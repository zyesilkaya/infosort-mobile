package com.example.deneme1.userAuth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController

import com.example.deneme1.HomePageActivity
import com.example.deneme1.LoginPageActivity
import com.example.deneme1.R
import com.example.deneme1.common.SharedPreferencesManager
import com.example.deneme1.databinding.FragmentSplashBinding
import com.parse.ParseException
import com.parse.ParseUser
import kotlinx.coroutines.*
import timber.log.Timber


class SplashFragment : Fragment(){

    private lateinit var binding: FragmentSplashBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSplashBinding.inflate(layoutInflater)
        autoLogin()
        return binding.root
    }


    private fun autoLogin(){
        val sp = SharedPreferencesManager.getInstance(requireContext())

        val usernameSP = sp.usernameSP
        val passwordSP = sp.passwordSP

        Timber.i("username: $usernameSP, password: $passwordSP")
        if( !usernameSP.isNullOrBlank()&&  !passwordSP.isNullOrBlank() ){
            ParseUser.logInInBackground(usernameSP,passwordSP) { parseUser: ParseUser?, e: ParseException? ->
                if (parseUser != null) {
                    goNavigationDrawerActivity()

                } else{
                    ParseUser.logOut()
                    goLoginFragment()
                    // if (e != null) { }
                }
            }
        }else{
            waitAndGoLoginFragment(1000)
        }
    }

    private fun waitAndGoLoginFragment(waitingTime:Long){
        CoroutineScope(Dispatchers.IO).launch {
            delay(waitingTime)
            withContext(Dispatchers.Main) {
                goLoginFragment()
            }
        }

    }

    private fun goLoginFragment(){
        findNavController().navigate(R.id.splashToLogin,null, NavOptions.Builder().setPopUpTo(findNavController().graph.startDestinationId, true).build())

    }

    private fun goNavigationDrawerActivity(){
        val intent = Intent(requireContext(), HomePageActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }

}