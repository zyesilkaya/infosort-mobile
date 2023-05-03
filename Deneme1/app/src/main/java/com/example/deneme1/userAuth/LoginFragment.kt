package com.example.deneme1.userAuth

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.deneme1.HomePageActivity
import com.example.deneme1.common.SharedPreferencesManager
import com.example.deneme1.databinding.FragmentLoginBinding
import com.parse.ParseException
import com.parse.ParseUser
import timber.log.Timber


class LoginFragment : Fragment() {
    private lateinit var btnLogin:androidx.appcompat.widget.AppCompatButton
    private lateinit var btnSignUp:androidx.appcompat.widget.AppCompatButton
    private lateinit var progressBar:com.google.android.material.progressindicator.CircularProgressIndicator

    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        uiPart()
        return binding.root
    }


    private fun login(username: String, password: String, remember: Boolean) {
        ParseUser.logInInBackground(username,password) { parseUser: ParseUser?, parseException: ParseException? ->
            if (parseUser != null) {

                if (remember) {
                    SharedPreferencesManager.getInstance(requireContext()).setUsernameAndPassword(username, password)
                }
                showAlert("Successful Login", "Welcome back $username !")
            } else {
                ParseUser.logOut()
                if (parseException != null) {
                    Toast.makeText(requireContext(), parseException.message, Toast.LENGTH_LONG)
                        .show()
                    setEnable(true)
                }
            }
            progressBar.visibility = View.GONE;
        }
    }

    private fun showAlert(title: String, message: String) {
        AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK") { dialog, which ->
                dialog.cancel()
                goToActivity()
            }.show()
    }

    private fun uiPart(){
        btnSignUp = binding.btnSignUp
        btnLogin = binding.btnLogin
        progressBar = binding.loading

        btnLogin.setOnClickListener{
            val remember = binding.cbRememberMe.isChecked
            setEnable(false)
            progressBar?.visibility = View.VISIBLE;

            if(isValidInput(binding.etUsername?.text.toString(),binding.etPassword?.text.toString()
                )){
                login(binding.etUsername?.text.toString(),binding.etPassword?.text.toString(),remember)
            }
        }

    }

    private fun isValidInput(password: String, username: String): Boolean {
        if(username==""|| password=="") {
            Timber.i("username: $username")
            Toast.makeText(requireContext(), "Bu alanlar boş bırakılamaz!", Toast.LENGTH_LONG)
                .show()
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

    private fun goToActivity(){
        val intent = Intent(requireContext(), HomePageActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }
}