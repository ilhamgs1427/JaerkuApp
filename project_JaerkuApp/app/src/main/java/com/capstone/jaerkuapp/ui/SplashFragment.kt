package com.capstone.jaerkuapp.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import com.capstone.jaerkuapp.R


class SplashFragment : Fragment() {
    companion object {
        private const val DURATION: Long = 1500
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val SPLASH_TIME_OUT = 2000

        Handler(Looper.getMainLooper()).postDelayed({
            val newFragment = DashboardFragment()
            val transaction: FragmentTransaction =
                requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(
                R.id.nav_host_fragment_activity_dashboard,
                newFragment
            )
            transaction.addToBackStack(null)
            transaction.commit()
        }, SPLASH_TIME_OUT.toLong())
    }
}