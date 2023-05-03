package com.example.deneme1

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.parse.ParseUser
import com.parse.SignUpCallback
import androidx.databinding.DataBindingUtil
import com.example.deneme1.databinding.ActivitySignupPageBinding


class SignupPageActivity: AppCompatActivity() {
    private lateinit var password:String
    private lateinit var passwordAgain:String
    private lateinit var username: String
    private lateinit var progressBar: com.google.android.material.progressindicator.CircularProgressIndicator
    private lateinit var binding : ActivitySignupPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        uiPart()
    }

    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            Log.i("MainActivity","INTENT")
            // There are no request codes
        }
    }

    private fun signup(password: String, username: String) {
        //TODO ismin uzunluğunun min sınırı max sınırı olmalı,
        progressBar.visibility = View.VISIBLE
        ParseUser().apply {
            setPassword(password)
            setUsername(username)
            signUpInBackground(SignUpCallback() {
                if(it == null){
                    progressBar.visibility = View.GONE
                    showAlert("Successful Sign Up!", "Welcome $username !");
                }
                else{
                    setEnable(true)
                    progressBar.visibility = View.GONE
                    ParseUser.logOut()
                    Toast.makeText(this@SignupPageActivity, it.message,Toast.LENGTH_LONG)
                }
            })
        }
    }

    private fun showAlert(title: String, message: String) {
        AlertDialog.Builder(this).apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton("OK"){ dialog, which ->
                dialog.cancel()
                val intent = Intent(this@SignupPageActivity, LoginPageActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                resultLauncher.launch(intent)
            }
        }.show()
    }

    private fun uiPart(){
        binding = DataBindingUtil.setContentView(this, R.layout.activity_signup_page)
        progressBar = binding.loading

        binding.btnSignUp.setOnClickListener{
            password = binding.etPassword?.text.toString()
            passwordAgain = binding.etPasswordAgain?.text.toString()
            username = binding.etUsername?.text.toString()
            setEnable(false)
            if(isValidInput()){
                signup(password,username)
            }else{
                setEnable(true)
                println(password.equals(passwordAgain))
            }
        }

        binding.btnLogin.setOnClickListener {
            val intent = Intent(this,LoginPageActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            resultLauncher.launch(intent)
        }

    }

    private fun isValidInput(): Boolean {
        if(username.equals("") || password.equals("") || passwordAgain.equals("")){
            Toast.makeText(this,"Bu alanlar boş bırakılamaz!",Toast.LENGTH_LONG).show()
            return false
        }
        else if(!password.equals(passwordAgain)){
            Toast.makeText(this,"Şifreler eşleşmiyor!",Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }

    private fun setEnable(bool:Boolean){
        with(binding){
            etUsername.isEnabled = bool
            etPassword.isEnabled = bool
            etPasswordAgain.isEnabled = bool
            btnLogin.isEnabled = bool
            btnSignUp.isEnabled = bool
        }
    }
}