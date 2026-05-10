package com.ingwoj.bookscanner

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.ingwoj.bookscanner.databinding.FirstWelcomeLayoutBinding
import com.ingwoj.bookscanner.databinding.MainLayoutBinding
import com.ingwoj.bookscanner.menuSections.homeSection.HomeFragment
import com.ingwoj.bookscanner.menuSections.userProfileSection.UserProfileFragment
import com.ingwoj.bookscanner.search_menu.SearchMenuFragment
import com.ingwoj.bookscanner.userServices.registration.RegisterActivity
import com.ingwoj.bookscanner.userServices.login.LoginActivity

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private var welcomeBinding: FirstWelcomeLayoutBinding? = null
    private var mainBinding: MainLayoutBinding? = null

    //TODO: change to MVVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        auth = Firebase.auth

        // Check if user is signed in
        val currentUser = auth.currentUser
        if (currentUser == null){
            welcomeBinding = FirstWelcomeLayoutBinding.inflate(layoutInflater)
            setContentView(welcomeBinding!!.root)

            // listener for login button
            welcomeBinding!!.loginWelcomeBtn.setOnClickListener{
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }

            // listener for register button
            welcomeBinding!!.registerWelcomeBtn.setOnClickListener{
                val intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)
            }
        }
        else {
            mainBinding = MainLayoutBinding.inflate(layoutInflater)
            setContentView(mainBinding!!.root)

            val bottomNav = mainBinding!!.bottomNavigation

            // TODO: fix reactions for navbar buttons
            bottomNav.setOnItemSelectedListener { item ->
                when(item.itemId) {
                    R.id.home_nav_btn -> {
                        loadFragment(HomeFragment(), R.id.main_fragment_container)
                        true
                    }
                    R.id.search_nav_btn -> {
                        loadFragment(SearchMenuFragment(), R.id.main_fragment_container)
                        true
                    }
                    R.id.library_nav_btn -> {
                        // Respond to navigation item 2 click
                        //TODO: library menu
                        true
                    }
                    R.id.profile_nav_btn -> {
                        loadFragment(UserProfileFragment(), R.id.main_fragment_container)
                        true
                    }
                    else -> false
                }
            }
        }
    }

    //    TODO: duplicate function in RegisterActivity!!!
    fun loadFragment(fragment: Fragment, containerView: Int){
        val fragmentManager = supportFragmentManager

        val fragmentTransaction : FragmentTransaction =
            fragmentManager.beginTransaction()

        fragmentTransaction.replace(containerView, fragment)
        fragmentTransaction.commit()
    }
}