package com.example.deneme1

import android.app.Application
import com.google.firebase.database.FirebaseDatabase
import com.parse.Parse
import com.parse.ParseInstallation
import timber.log.Timber


class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Parse.initialize(
            Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.back4app_app_id))
                .clientKey(getString(R.string.back4app_client_key))
                .server(getString(R.string.back4app_server_url))
                .build()
        )
        val installation = ParseInstallation.getCurrentInstallation()
        installation.put("GCMSenderId", "908340410579")
        installation.saveInBackground()

        // Write a message to the database
        // Write a message to the database
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("message")

        myRef.setValue("Hello, World!")

        if(BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}