package com.example.bookhub.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.navigation.NavigationView
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import android.widget.FrameLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.bookhub.fragment.DashboardFragment
import com.example.bookhub.fragment.FavoriteFragment
import com.example.bookhub.fragment.ProfileFragment
import com.example.bookhub.R
import com.example.bookhub.fragment.AboutFragment

class MainActivity : AppCompatActivity() {

    lateinit var drawerLayout: DrawerLayout
    lateinit var toolbar: Toolbar
    lateinit var navbar: NavigationView
    lateinit var frameLayout: FrameLayout
    lateinit var coordinatorLayout: CoordinatorLayout
    var prevMenuItem: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout = findViewById(R.id.drawerlayout)
        toolbar = findViewById(R.id.toolbaar)
        navbar = findViewById(R.id.navbar)
        frameLayout = findViewById(R.id.framelayout)
        coordinatorLayout = findViewById(R.id.coordinator)

        setUpToolbar()
        openDashboard()

        navbar.setNavigationItemSelectedListener {
            if (prevMenuItem != null) {
                prevMenuItem?.isChecked = false
        }

            it.isCheckable = true
            it.isChecked = true
            prevMenuItem = it

            when (it.itemId) {
                R.id.dahboard -> {
                    openDashboard()
                    drawerLayout.closeDrawers()
                }

                R.id.profile -> {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.framelayout,
                        ProfileFragment()
                    ).commit()
                    supportActionBar?.title = "Profile"
                    drawerLayout.closeDrawers()
                }

                R.id.favorite -> {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.framelayout,
                        FavoriteFragment()
                    ).commit()
                    supportActionBar?.title = "Favourite"
                    drawerLayout.closeDrawers()
                }

                R.id.aboutus -> {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.framelayout,
                        AboutFragment()
                    ).commit()
                    supportActionBar?.title = "About Us"
                    drawerLayout.closeDrawers()
                }

            }
            return@setNavigationItemSelectedListener true
        }

        val actionBarDrawerToggle =
            ActionBarDrawerToggle(this, drawerLayout, R.string.open_Drawer, R.string.close_drawer)
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
    }

    private fun setUpToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "BookHub"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun openDashboard() {
        supportFragmentManager.beginTransaction().replace(R.id.framelayout, DashboardFragment())
            .commit()
        supportActionBar?.title = "Dashboard"
        navbar.setCheckedItem(R.id.dahboard)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        when (supportFragmentManager.findFragmentById(R.id.framelayout)) {
            !is DashboardFragment -> {
                openDashboard()
            }
            else -> super.onBackPressed()
        }
    }
}