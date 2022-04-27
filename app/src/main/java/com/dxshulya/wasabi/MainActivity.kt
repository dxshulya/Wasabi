package com.dxshulya.wasabi

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.dxshulya.wasabi.databinding.ActivityMainBinding
import com.dxshulya.wasabi.datastore.SharedPreference
import com.google.android.material.navigation.NavigationView
import com.google.android.material.switchmaterial.SwitchMaterial
import java.security.acl.Owner


class MainActivity : AppCompatActivity(), NavController.OnDestinationChangedListener,
    DrawerLayout.DrawerListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var badgeCounter: TextView
    private lateinit var mainActivityViewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainActivityViewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]

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
        val themeSwitcher = header.findViewById<SwitchMaterial>(R.id.theme_switcher)

        headerName.text = mainActivityViewModel.sharedPreference.name
        headerEmail.text = mainActivityViewModel.sharedPreference.email

        themeSwitcher.isChecked = mainActivityViewModel.sharedPreference.isDarkMode

        themeSwitcher.setOnCheckedChangeListener { _, _ ->
            if (themeSwitcher.isChecked) {
                mainActivityViewModel.changeMode(true)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                mainActivityViewModel.changeMode(false)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_task, R.id.nav_favorite, R.id.exit
            ), drawerLayout
        )

        navController.addOnDestinationChangedListener(this)
        drawerLayout.addDrawerListener(this)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navView.menu.findItem(R.id.exit).setOnMenuItemClickListener {
            if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            }
            mainActivityViewModel.sharedPreference.token = ""
            mainActivityViewModel.sharedPreference.email = ""
            mainActivityViewModel.sharedPreference.name = ""
            mainActivityViewModel.sharedPreference.password = ""
            findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.loginFragment)
            true
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        val counterFavorites = menu.findItem(R.id.nav_favorite)

        if (mainActivityViewModel.sharedPreference.totalCount == 0) {
            counterFavorites.actionView = null
        } else {
            counterFavorites.setActionView(R.layout.custom_badge_layout)
            val view = counterFavorites.actionView
            badgeCounter = view.findViewById(R.id.badge_counter)
            mainActivityViewModel.countLiveData.observe(this, Observer {
                badgeCounter.text = it
                Handler().postDelayed({
                    mainActivityViewModel.getTotalCount()
                }, 5000)
            })
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val currentItem = item.itemId

        if (currentItem == R.id.nav_favorite) {
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

    override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
        val navView: NavigationView = binding.navView
        val header = navView.getHeaderView(0)
        val headerName = header.findViewById<TextView>(R.id.header_name)
        val headerEmail = header.findViewById<TextView>(R.id.header_email)
        headerName.text = mainActivityViewModel.sharedPreference.name
        headerEmail.text = mainActivityViewModel.sharedPreference.email
    }

    override fun onDrawerOpened(drawerView: View) {
        Log.i("OPEN", "1")
    }

    override fun onDrawerClosed(drawerView: View) {
        Log.i("CLOSE", "1")
    }

    override fun onDrawerStateChanged(newState: Int) {
        Log.i("STATE", "1")
    }
}