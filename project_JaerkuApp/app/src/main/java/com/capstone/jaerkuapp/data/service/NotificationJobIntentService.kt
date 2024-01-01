package com.capstone.jaerkuapp.data.service

import android.content.Context
import android.content.Intent
import androidx.core.app.JobIntentService

// Buat kelas servis
class NotificationJobIntentService : JobIntentService() {
    companion object {
        private const val JOB_ID = 1000
        private const val EXTRA_NEW_KONDISI = "extra_new_kondisi"

        fun enqueueWork(context: Context, newKondisi: String?) {
            val work = Intent(context, NotificationJobIntentService::class.java).apply {
                putExtra(EXTRA_NEW_KONDISI, newKondisi)
            }
            enqueueWork(context, NotificationJobIntentService::class.java, JOB_ID, work)
        }
    }

    override fun onHandleWork(intent: Intent) {
        // Lakukan pengecekan kondisi dan tampilkan notifikasi di sini
        val newKondisi = intent.getStringExtra(EXTRA_NEW_KONDISI)
        if (newKondisi != null && newKondisi != "normal") {
            showNotification("PERINGATAN!!!", "Kondisi air: $newKondisi")
            }
    }

    private fun showNotification(title: String, content: String) {
        val notificationHelper = NotificationHelper(applicationContext)
        notificationHelper.showNotification(title, content)
    }
}