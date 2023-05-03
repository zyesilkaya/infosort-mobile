package com.example.deneme1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.deneme1.common.SharedPreferencesManager
import com.example.deneme1.databinding.ActivityHomePageBinding
import com.google.android.material.navigation.NavigationView
import com.parse.ParseException
import com.parse.ParseUser
import timber.log.Timber

class HomePageActivity : AppCompatActivity(){
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityHomePageBinding
    lateinit var navHostFragment: NavHostFragment
    lateinit var navView: NavigationView
    lateinit var navController:NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar2)

        navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment

        val drawerLayout: DrawerLayout = binding.drawerLayout
        navView = binding.navigationView
        navController = navHostFragment.navController

        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.order,
            R.id.profile,
            R.id.menu
        ), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        binding.logOut.setOnClickListener {
            logOut()
        }

        //TODO headerdaki sayılar ve yazılar için
        /*
         val viewHeader = navView.getHeaderView(0)
        val navViewHeaderBinding : NavHeaderBinding = NavHeaderBinding.bind(viewHeader)
        navViewHeaderBinding.user = ParseUser.getCurrentUser()

        navViewHeaderBinding.viewmodel=navigationDrawerMainViewModel
        navViewHeaderBinding.lifecycleOwner = this
         */
        //TODO usertype a göre hideItem yap

        val userType = ParseUser.getCurrentUser().get("level")
        hideItem()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun logOut(){
        ParseUser.logOutInBackground { e: ParseException? ->
            if (e == null) {
                SharedPreferencesManager.getInstance(this).removeUsernameAndPassword()
                val intent = Intent(this, LoginPageActivity::class.java)
                startActivity(intent)
                finish()

            }else{
                Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }
    private fun hideItem() {
        val navMenu: Menu = navView.menu
        navMenu.findItem(R.id.dmEmployees).isVisible = false
    }
}

