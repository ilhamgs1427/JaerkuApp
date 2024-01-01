package com.capstone.jaerkuapp.ui


import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.capstone.jaerkuapp.R
import com.capstone.jaerkuapp.data.service.NotificationHelper
import com.capstone.jaerkuapp.data.remote.DataResult
import com.capstone.jaerkuapp.data.remote.response.DataItem
import com.capstone.jaerkuapp.data.service.NotificationJobIntentService
import com.capstone.jaerkuapp.databinding.FragmentDashboardBinding
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

class DashboardFragment : Fragment() {



   private lateinit var  _binding: FragmentDashboardBinding
   private val binding get() = _binding
    private val viewModel:DashboardViewModel by viewModels {
        ViewModelFactory(requireActivity())
    }
    private val handler = Handler(Looper.getMainLooper())
    private val delayMillis = 60000
    private  val NOTIFICATION_PERMISSION_REQUEST_CODE = 123

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        requestNotificationPermission()
        viewModel.fuzzyData.observe(viewLifecycleOwner) {
            val currentKondisi = it.sensorData.kondisi
            binding.tvResult.text = it.sensorData.kondisi
            binding.tvPh.text = it.sensorData.pHValue
            binding.tvTds.text = it.sensorData.tDSValue
            binding.tvTurbidity.text = it.sensorData.turbidityValue
            val bundle = Bundle()
            bundle.putString(DetailFragment.EXTRA_NAME, it.sensorData.kondisi)
            bundle.putString(DetailFragment.EXTRA_DESC, it.sensorData.keterangan)
            binding.cardFuzzy.setOnClickListener {
                val fragment = DetailFragment()
                fragment.arguments = bundle
                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                transaction.replace(R.id.nav_host_fragment_activity_dashboard,fragment)
                transaction.addToBackStack(null)
                transaction.commit()
            }
            checkAndShowNotification(currentKondisi)
        }
//        viewModel.getFuzzyData()


        viewModel.sensorList.observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is DataResult.Success -> {
                        val dataItems = result.data
                        Log.d("DashboardFragment", "Data items: ${dataItems}")
                        phChart(dataItems)
                        tdsChart(dataItems)
                        turbidityChart(dataItems)
                        showLoading(false)
                    }

                    is DataResult.Error -> {
                        // Handle error
                        showLoading(false)
                        Log.d("viewmodel", "Error: ${result.error}")
                    }

                    is DataResult.Loading -> {
                       showLoading(true)
                    }
                }
            }else{
                Log.d("viewmodel","data null")
                showLoading(false)
            }
        }
        viewModel.getFuzzyData()
        viewModel.getSensorList()
        handler.postDelayed(object : Runnable {
            override fun run() {
                viewModel.getFuzzyData()
                viewModel.getSensorList()
                handler.postDelayed(this, delayMillis.toLong())
            }
        }, delayMillis.toLong())
    }
    private fun phChart(dataItems: List<DataItem>){

        val entries = dataItems.mapIndexedNotNull { index, dataItem ->
            Entry(index.toFloat(), dataItem.pHValue?.toFloat() ?: 0f)
        }
        Log.d("DashboardFragment", "Entries: ${entries}")

        val dataSet = LineDataSet(entries, "pH Values")
        dataSet.setDrawValues(false)

        val lineData = LineData(dataSet)

        setupChart(binding.phLineChart)
        binding.phLineChart.data = lineData
        binding.phLineChart.animateXY(2000,2000)
        binding.phLineChart.invalidate()
    }
    private fun tdsChart(dataItems: List<DataItem>){
        val entries = dataItems.mapIndexedNotNull { index, dataItem ->
            Entry(index.toFloat(), dataItem.tDSValue?.toFloat() ?: 0f)
        }
        Log.d("DashboardFragment", "Entries: ${entries}")
        val dataSet = LineDataSet(entries, "Tds Values")
        dataSet.setDrawValues(false)
        dataSet.color = resources.getColor(R.color.sec_blue1)

        val lineData = LineData(dataSet)

        setupChart(binding.tdsLineChart)

        binding.tdsLineChart.data = lineData
        binding.tdsLineChart.animateXY(2000,2000)
        binding.tdsLineChart.invalidate()
    }

    private fun turbidityChart(dataItems: List<DataItem>){

        val entries = dataItems.mapIndexedNotNull { index, dataItem ->
            Entry(index.toFloat(), dataItem.turbidityValue?.toFloat() ?: 0f)
        }
        Log.d("DashboardFragment", "Entries: ${entries}")

        val dataSet = LineDataSet(entries, "Turbidity Values")
        dataSet.setDrawValues(false)
        dataSet.color = resources.getColor(R.color.sec_blue2)
        val lineData = LineData(dataSet)
        setupChart(binding.turbidityLineChart)
        binding.turbidityLineChart.data = lineData
        binding.turbidityLineChart.animateXY(2000,2000)
        binding.turbidityLineChart.invalidate()
    }
    private fun setupChart(chart: LineChart) {
        chart.apply {
            description.isEnabled = false
            setTouchEnabled(true)
            isDragEnabled = true
            setPinchZoom(true)
            setDrawGridBackground(false)
            axisLeft.setDrawGridLines(false)
            axisRight.setDrawGridLines(false)
            xAxis.setDrawGridLines(false)
        }
    }
    private fun checkAndShowNotification(newKondisi: String?) {
        Log.d("DashboardFragment", "Nilai kondisi baru: $newKondisi")
        if (newKondisi != null && newKondisi != "normal") {
            showNotification("PERINGATAN!!!", "Kondisi air : $newKondisi")
            NotificationJobIntentService.enqueueWork(requireContext(), newKondisi)
            Log.d("mengirim notif :","terkirim")
            saveLastKnownKondisi(newKondisi)
        }
    }

    private fun showNotification(title: String, content: String) {
        Log.d("DashboardFragment", "Menampilkan notifikasi: $title - $content")
        val notificationHelper = NotificationHelper(requireContext())
        notificationHelper.showNotification(title, content)
    }

    private fun saveLastKnownKondisi(newKondisi: String) {
        val sharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString("lastKnownKondisi", newKondisi)
            apply()
        }
    }
    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.VIBRATE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(
                        Manifest.permission.VIBRATE,
                        Manifest.permission.USE_FULL_SCREEN_INTENT
                    ),
                    NOTIFICATION_PERMISSION_REQUEST_CODE
                )
            }
        }
    }
    private fun showLoading(state: Boolean){
        binding.progressBar.isVisible = state
    }
    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }
}
