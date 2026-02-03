package com.taboola.kotlin.examples

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.navigation.NavigationView
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private val applicationScreens = setOf(
        R.id.nav_classic_widget,
        R.id.nav_classic_widget_xml,
        R.id.nav_classic_feed,
        R.id.nav_classic_viewpager,
        R.id.nav_web_widget,
        R.id.nav_web_feed,
        R.id.nav_native_widget,
        R.id.nav_native_feed,
        R.id.nav_classic_explore_more,
        R.id.nav_classic_explore_more_compose
    )

    // Fragments that should become root screens (back button exits app)
    // Explore More auto trigger works only if the activity or fragment is the root screen
    private val rootScreenDestinations = setOf(
        R.id.nav_classic_explore_more,
        R.id.nav_classic_explore_more_compose
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Setup general Activity layout
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Setup interchanging screens navigation
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(applicationScreens, drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        
        // Get start destination ID from navigation graph (single source of truth)
        val startDestinationId = navController.graph.startDestination
        
        // Set up custom navigation listener to handle root screen destinations
        navView.setNavigationItemSelectedListener { menuItem ->
            if (rootScreenDestinations.contains(menuItem.itemId)) {
                // Clear back stack and navigate to root screen
                // Pop up to start destination (inclusive) to clear entire back stack
                val navOptions = NavOptions.Builder()
                    .setPopUpTo(startDestinationId, true)
                    .build()
                navController.navigate(menuItem.itemId, null, navOptions)
            } else {
                // Use default navigation behavior
                navController.navigate(menuItem.itemId)
            }
            drawerLayout.closeDrawers()
            true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}