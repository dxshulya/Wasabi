package com.dxshulya.wasabi

import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.GravityCompat
import androidx.core.view.MenuItemCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.dxshulya.wasabi.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import com.google.android.material.switchmaterial.SwitchMaterial


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

        badgeCounter = MenuItemCompat.getActionView(
            navView.menu.findItem(R.id.nav_favorite)
        ) as TextView
        initCountDrawer()

        navView.menu.findItem(R.id.exit).setOnMenuItemClickListener {
            if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            }
            mainActivityViewModel.sharedPreference.token = ""
            mainActivityViewModel.sharedPreference.email = ""
            mainActivityViewModel.sharedPreference.name = ""
            mainActivityViewModel.sharedPreference.password = ""
            mainActivityViewModel.sharedPreference.totalCount = 0
            findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.loginFragment)
            true
        }
    }

    private fun initCountDrawer() {
        badgeCounter.gravity = Gravity.CENTER_VERTICAL
        badgeCounter.setTypeface(null, Typeface.BOLD)
        badgeCounter.setTextColor(resources.getColor(R.color.red))
        badgeCounter.text = mainActivityViewModel.sharedPreference.totalCount.toString()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
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
        badgeCounter = MenuItemCompat.getActionView(
            navView.menu.findItem(R.id.nav_favorite)
        ) as TextView
        initCountDrawer()
    }

    override fun onDrawerOpened(drawerView: View) {
        val navView: NavigationView = binding.navView
        badgeCounter = MenuItemCompat.getActionView(
            navView.menu.findItem(R.id.nav_favorite)
        ) as TextView
        initCountDrawer()
    }

    override fun onDrawerClosed(drawerView: View) {
        val navView: NavigationView = binding.navView
        badgeCounter = MenuItemCompat.getActionView(
            navView.menu.findItem(R.id.nav_favorite)
        ) as TextView
        initCountDrawer()
    }

    override fun onDrawerStateChanged(newState: Int) {
        val navView: NavigationView = binding.navView
        badgeCounter = MenuItemCompat.getActionView(
            navView.menu.findItem(R.id.nav_favorite)
        ) as TextView
        initCountDrawer()
    }
}