package com.synagoguemanagement.synagoguemanagement

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
//import com.example.yoursynagogue.SigninFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.FirebaseApp
import com.synagoguemanagement.synagoguemanagement.databinding.ActivityMainBinding
import com.synagoguemanagement.synagoguemanagement.ui.book.BookSeatsFragment
import com.synagoguemanagement.synagoguemanagement.ui.messages.MessagesFragment
import com.synagoguemanagement.synagoguemanagement.ui.prayer.PrayerTimeFragment
import com.synagoguemanagement.synagoguemanagement.ui.shabbatentry.ShabbatEntryFragment
import com.google.firebase.auth.FirebaseAuth
import com.synagoguemanagement.synagoguemanagement.ui.profile.EditProfileFragment
import com.synagoguemanagement.synagoguemanagement.ui.signin.SigninFragment
import com.synagoguemanagement.synagoguemanagement.ui.signin.SignupFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setToolBar()

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        if (!isUserLoggedIn()) {
            loadFragment(SigninFragment()) // Show Sign-in page first
            bottomNavigationView.visibility = android.view.View.GONE
        } else {
            loadFragment(ShabbatEntryFragment()) // Show main page if logged in
            bottomNavigationView.visibility = android.view.View.VISIBLE
        }

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

        // Initialize Firebase
        FirebaseApp.initializeApp(this)

        // Get instance of FirebaseAuth
        auth = FirebaseAuth.getInstance()
    }

    private fun setToolBar() {
        val toolbar = findViewById<Toolbar>(R.id.top_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayShowTitleEnabled(true)
            title = "Synagogue"
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.synagogue_24px)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        menu.findItem(R.id.action_sign_out)?.title = "Sign Out"
        return true
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.visibility = if (fragment is SigninFragment || fragment is SignupFragment) {
            android.view.View.GONE
        } else {
            android.view.View.VISIBLE
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_edit_profile -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, EditProfileFragment())
                    .addToBackStack(null)
                    .commit()
                true
            }
            R.id.action_sign_out -> {
                signOutUser()
                true
            }
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun signOutUser() {
        val sharedPref = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        sharedPref.edit().putBoolean("isLoggedIn", false).apply()

        Toast.makeText(this, "Signed out", Toast.LENGTH_SHORT).show()
        openSignInPage()
    }

    private fun openSignInPage() {
        loadFragment(SigninFragment())
    }

    private fun isUserLoggedIn(): Boolean {
        val sharedPref = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        return sharedPref.getBoolean("isLoggedIn", false)
    }

    fun onUserLoggedIn() {
        val sharedPref = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        sharedPref.edit().putBoolean("isLoggedIn", true).apply()

        loadFragment(ShabbatEntryFragment())
        findViewById<BottomNavigationView>(R.id.bottom_navigation).visibility = android.view.View.VISIBLE
    }
}
