package com.capstone.jaerkuapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.capstone.jaerkuapp.databinding.FragmentDetailBinding


class DetailFragment : Fragment() {
    private lateinit var  _binding: FragmentDetailBinding
    private val binding get() = _binding!!
    private  var name: String = ""
    private  var desc: String = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        arguments.let {
            name = it!!.getString(EXTRA_NAME, "")
            desc = it!!.getString(EXTRA_DESC, "")
        }
        binding.tvResult.text = name
        binding.tvDesc.text = desc
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Kembali ke HomeFragment saat tombol back ditekan
                parentFragmentManager.popBackStack()
            }
        })

    }
    companion object{
        const val EXTRA_NAME = "extra_name"
        const val EXTRA_DESC = "extra_desc"
    }


}