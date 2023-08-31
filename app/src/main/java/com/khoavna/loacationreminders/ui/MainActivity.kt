package com.khoavna.loacationreminders.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupActionBarWithNavController
import com.khoavna.loacationreminders.R
import com.khoavna.loacationreminders.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val topLevelDestinationIds = setOf(R.id.loginFragment)

    private lateinit var navController: NavController
    private val appBarConfig = AppBarConfiguration(
        topLevelDestinationIds
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        navController = supportFragmentManager.findFragmentById(R.id.nav_host_fragment).let {
            (it as NavHostFragment).findNavController()
        }.apply {
            addOnDestinationChangedListener { _, des, _ ->
                if (des.id in topLevelDestinationIds) {
                    binding.toolbar.isVisible = false
                }
            }
        }

        setupActionBarWithNavController(navController, appBarConfig)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }
}