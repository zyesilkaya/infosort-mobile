package com.example.deneme1

import com.parse.ParseException
import com.parse.ParseUser
import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.databinding.DataBindingUtil
import com.example.deneme1.common.SharedPreferencesManager
import com.example.deneme1.databinding.ActivityLoginBinding

class LoginPageActivity : AppCompatActivity() {

    private lateinit var btnLogin:androidx.appcompat.widget.AppCompatButton
    private lateinit var btnSignUp:androidx.appcompat.widget.AppCompatButton
    private lateinit var progressBar:com.google.android.material.progressindicator.CircularProgressIndicator
    private lateinit var username: String
    private lateinit var password: String
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        uiPart()
    }

    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            Log.i("MainActivity","INTENT")
            // There are no request codes
        }
    }

    private fun login(username: String, password: String, remember: Boolean) {
        ParseUser.logInInBackground(username,password) { parseUser: ParseUser?, parseException: ParseException? ->
            if (parseUser != null) {
                if (remember){
                    SharedPreferencesManager.getInstance(this).setUsernameAndPassword(username,password)
                    }
                showAlert("Successful Login", "Welcome back $username !")
            } else {
                ParseUser.logOut()
                if (parseException != null) {
                    Toast.makeText(this, "Kullanıcı adı veya şifre hatalı", Toast.LENGTH_LONG)
                        .show()
                    setEnable(true)
                }
            }

            progressBar?.visibility = View.GONE;
            }


    }

    private fun showAlert(title: String, message: String) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK") { dialog, which ->
                dialog.cancel()
                val intent = Intent(this, HomePageActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                resultLauncher.launch(intent)
            }.show()
    }

    private fun uiPart(){
        btnSignUp = binding.btnSignUp
        btnLogin = binding.btnLogin
        progressBar = binding.loading

        btnLogin.setOnClickListener{
            username = binding.etUsername?.text.toString()
            password = binding.etPassword?.text.toString()
            val remember = binding.cbRememberMe.isChecked
            setEnable(false)
            progressBar?.visibility = View.VISIBLE;

            if(isValidInput(username, password)){
                login(username, password,remember)
            }
        }

        btnSignUp.setOnClickListener {
            val intent = Intent(this, SignupPageActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            resultLauncher.launch(intent)
        }
    }

    private fun isValidInput(password: String, username: String): Boolean {
        if(username==""|| password==""){
            Log.i("MainActivity",username)
            Toast.makeText(this,"Bu alanlar boş bırakılamaz!",Toast.LENGTH_LONG).show()
            progressBar?.visibility = View.GONE;
            setEnable(true)
            return false
        }
        return true
    }

    private fun setEnable(bool:Boolean){
        with(binding){
            etUsername.isEnabled = bool
            etPassword.isEnabled = bool
            btnLogin.isEnabled = bool
            btnSignUp.isEnabled = bool
        }
    }
}