package com.dxshulya.wasabi

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import com.dxshulya.wasabi.databinding.ActivityMainBinding
import com.dxshulya.wasabi.datastore.SharedPreference

class MainActivity : AppCompatActivity(), NavController.OnDestinationChangedListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        val header = navView.getHeaderView(0)
        val headerName = header.findViewById<TextView>(R.id.header_name)
        val headerEmail = header.findViewById<TextView>(R.id.header_email)
        val sharedPreference = SharedPreference(this)

        headerName.text = sharedPreference.name
        headerEmail.text = sharedPreference.email

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_task, R.id.nav_favorite, R.id.exit
            ), drawerLayout
        )

        navController.addOnDestinationChangedListener(this)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val currentItem = item.itemId

        if (currentItem == R.id.nav_favorite){
            findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.nav_favorite)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        when (destination.id) {
            R.id.splashFragment, R.id.registrationFragment, R.id.loginFragment, R.id.introFragment -> {
                binding.toolbar.visibility = View.GONE
            }
            R.id.exit -> {
//                this.findNavController(R.id.nav_host_fragment_content_main)
//                    .navigate(R.id.action_splashFragment_to_registrationFragment)
            }
            else -> {
                binding.toolbar.visibility = View.VISIBLE
                binding.labelToolbar.text = destination.label
            }
        }
    }


    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}