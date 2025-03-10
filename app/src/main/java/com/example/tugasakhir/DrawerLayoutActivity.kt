package com.example.tugasakhir

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class DrawerLayoutActivity : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener{

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.drawer_layout)

        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_drawer_view)

        // Setup Toolbar
        setSupportActionBar(findViewById(R.id.toolbar))

        // Prevent closing the drawer when clicking on header or any other view
        navigationView.getHeaderView(0).setOnClickListener {
            // Do nothing here to prevent automatic drawer closing
        }
        navigationView.setNavigationItemSelectedListener(this)
    }

    // Override onBackPressed to close drawer on back press if it's open
    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(navigationView)) {
            drawerLayout.closeDrawer(navigationView)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navProfil -> {
                val intent = Intent(this, Profil::class.java)
                startActivity(intent)
            }
            R.id.navTiket -> {
                val intent = Intent(this, TiketActivity::class.java)
                startActivity(intent)
            }
            // Handle other navigation items if necessary
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}
