package com.example.tugasakhir
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.tugasakhir.databinding.ActivityMenuBinding

class menu : AppCompatActivity() {

    private lateinit var binding: ActivityMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve intent data from TicketActivity

        // Navigation setup
        val navController = findNavController(R.id.nav_host_fragment_activity_menu)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            ), binding.drawerLayout
        )

        // Setup navigation view with NavController
        binding.navDrawerView.setupWithNavController(navController)

        // Setup bottom navigation view
        binding.navView.setupWithNavController(navController)

        // Find the DrawerLayout
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)

        // Find the toolbar image button and set OnClickListener to open the drawer
        binding.toolbarImageButton.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_activity_menu)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}
