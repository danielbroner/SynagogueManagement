package com.synagoguemanagement.synagoguemanagement

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.yoursynagogue.SigninFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.synagoguemanagement.synagoguemanagement.databinding.ActivityMainBinding
import com.synagoguemanagement.synagoguemanagement.ui.book.BookSeatsFragment
import com.synagoguemanagement.synagoguemanagement.ui.messages.MessagesFragment
import com.synagoguemanagement.synagoguemanagement.ui.prayer.PrayerTimeFragment
import com.synagoguemanagement.synagoguemanagement.ui.shabbatentry.ShabbatEntryFragment

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setToolBar()

        val signInFragment = SigninFragment()
        loadFragment(signInFragment)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnItemSelectedListener { item ->
            val fragment: Fragment = when (item.itemId) {
                R.id.nav_messages -> MessagesFragment()
                R.id.nav_prayer_time -> PrayerTimeFragment()
                R.id.nav_book_seats -> BookSeatsFragment()
                R.id.nav_shabbat_entry -> ShabbatEntryFragment()
                else -> ShabbatEntryFragment()
            }
            loadFragment(fragment)
            true
        }
    }

    private fun setToolBar() {
        val toolbar = findViewById<Toolbar>(R.id.top_toolbar)
        setSupportActionBar(toolbar)

        // Set Icon and Title
        supportActionBar?.apply {
            setDisplayShowTitleEnabled(true)
            title = "Synagogue"
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.synagogue_24px) // Synagogue icon
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        val item = menu.findItem(R.id.action_sign_out)
        item?.title = "Sign Out"
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_sign_out -> {
                signOutUser()
                true
            }
            android.R.id.home -> {  // Handle back button if needed
                onBackPressedDispatcher.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun signOutUser() {
        Toast.makeText(this, "Signed out", Toast.LENGTH_SHORT).show()
        openSignInPage()
        // Add logout logic, such as clearing session data or Firebase sign-out
    }

    private fun openSignInPage() {
        loadFragment(SigninFragment())
    }
}