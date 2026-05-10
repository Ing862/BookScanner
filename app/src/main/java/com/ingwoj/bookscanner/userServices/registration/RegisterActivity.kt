package com.ingwoj.bookscanner.userServices.registration

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.snackbar.Snackbar
import com.ingwoj.bookscanner.userServices.registration.UserRegistrationViewModel
import com.ingwoj.bookscanner.MainActivity
import com.ingwoj.bookscanner.R
import com.ingwoj.bookscanner.databinding.RegisterMainLayoutBinding
import kotlinx.coroutines.launch

class RegisterActivity: AppCompatActivity() {
    private lateinit var binding: RegisterMainLayoutBinding

    // initializing viewModel
    private val viewModel: UserRegistrationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = RegisterMainLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // loading fragment with user data input
        val registerDataFragment = RegisterDataFragment()
        loadFragment(registerDataFragment)

        // if back btn clicked (in data fragment)
        binding.backBtn.setOnClickListener {
            if (supportFragmentManager.findFragmentById(R.id.register_fragment_container) is RegisterDataFragment) finish()
            else loadFragment(registerDataFragment)
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.registrationFormState.collect { state ->
                        binding.nextBtn.isEnabled = state.isButtonEnabled
                    }
                }

                launch {
                    viewModel.uiState.collect { state ->
                        when (state) {
                            is UserRegistrationViewModel.UIRegistrationState.Loading -> showLoading(true)
                            is UserRegistrationViewModel.UIRegistrationState.Success -> navigateToHome()
                            is UserRegistrationViewModel.UIRegistrationState.Error -> {
                                showLoading(false)
                                Snackbar.make(binding.root, state.message, Snackbar.LENGTH_LONG).show()
                            }
                            else -> null
                        }
                    }
                }
            }
        }

        // TODO: fix registration (next button)
        binding.nextBtn.setOnClickListener {
            val currentFragment = supportFragmentManager.findFragmentById(R.id.register_fragment_container)

            when (currentFragment) {
                is RegisterDataFragment ->{
                    loadFragment(RegisterPasswordFragment())
                    viewModel.disableButton()
                }
                is RegisterPasswordFragment -> {
                    viewModel.registerUser(
                        viewModel.registrationFormState.value.data.email,
                        viewModel.registrationFormState.value.data.password,
                        this)

//                    // TODO: fix
//                    FirebaseAuth.getInstance().addAuthStateListener { auth ->
//                        if (auth.currentUser != null) {
//                            val intent = Intent(this, MainActivity::class.java)
//                            intent.flags =
//                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                            startActivity(intent)
//                        }
//                    }
                }
            }
        }
    }

    fun loadFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager

        val fragmentTransaction: FragmentTransaction =
            fragmentManager.beginTransaction()

        fragmentTransaction.replace(R.id.register_fragment_container, fragment)
        fragmentTransaction.commit()
    }

    fun navigateToHome(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun showLoading(show: Boolean) {
        binding.loadingScreen.visibility = if (show) View.VISIBLE else View.GONE
    }
}