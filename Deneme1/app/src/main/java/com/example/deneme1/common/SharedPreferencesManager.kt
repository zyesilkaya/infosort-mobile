package com.example.deneme1.common

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class SharedPreferencesManager private constructor(context: Context) {

    private val masterKeyAlias = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPrefs: SharedPreferences = EncryptedSharedPreferences.create(
        context,
        FILE_NAME,
        masterKeyAlias,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun setUsernameAndPassword(username: String, password:String ) {
        val editor = sharedPrefs.edit()
        editor.putString(KEY_FOR_USERNAME, username)
        editor.putString(KEY_FOR_PASSWORD, password)
        editor.apply()
    }

    fun removeUsernameAndPassword(){
        val editor = sharedPrefs.edit()
        editor.remove(KEY_FOR_USERNAME)
        editor.remove(KEY_FOR_PASSWORD)
        editor.apply()
    }


    val usernameSP: String?
        get() = sharedPrefs.getString(KEY_FOR_USERNAME,null)

    val passwordSP: String?
        get() = sharedPrefs.getString(KEY_FOR_PASSWORD,null)

    companion object {
        // değiştirebilirsin file name
        private const val FILE_NAME = "AppPrefsFile"
        private const val KEY_FOR_USERNAME = "USERNAME"
        private const val KEY_FOR_PASSWORD= "PASSWORD"

        private var instance: SharedPreferencesManager? = null
        @Synchronized
        fun getInstance(context: Context?): SharedPreferencesManager {
            if (instance == null) instance =
                context?.let { SharedPreferencesManager(it) }
            return instance!!
        }
    }

}

/*
Shared preference without encrypt

class SharedPreferencesManager private constructor(context: Context) {
    private val sharedPrefs: SharedPreferences = context.applicationContext.getSharedPreferences(
        APP_PREFS,
        Context.MODE_PRIVATE
    )

    fun setUsernameAndPassword(username: String, password:String) {
        val editor = sharedPrefs.edit()
        editor.putString(KEY_FOR_USERNAME, username)
        editor.putString(KEY_FOR_PASSWORD, password)
        editor.apply()
    }

    fun removeUsernameAndPassword(){
        val editor = sharedPrefs.edit()
        editor.remove(KEY_FOR_USERNAME)
        editor.remove(KEY_FOR_PASSWORD)
        editor.apply()
    }



    val usernameSP: String?
        get() = sharedPrefs.getString(KEY_FOR_USERNAME,null)

    val passwordSP: String?
        get() = sharedPrefs.getString(KEY_FOR_PASSWORD,null)

    companion object {
        private const val APP_PREFS = "AppPrefsFile"
        private const val KEY_FOR_USERNAME = "USERNAME"
        private const val KEY_FOR_PASSWORD= "PASSWORD"

        private var instance: SharedPreferencesManager? = null
        @Synchronized
        fun getInstance(context: Context?): SharedPreferencesManager {
            if (instance == null) instance =
                context?.let { SharedPreferencesManager(it) }
            return instance!!
        }
    }

}

 */