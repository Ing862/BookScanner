package com.ingwoj.bookscanner.userServices.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.isInvisible
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.snackbar.Snackbar
import com.ingwoj.bookscanner.userServices.login.LoginViewModel
import com.ingwoj.bookscanner.MainActivity
import com.ingwoj.bookscanner.R
import com.ingwoj.bookscanner.databinding.LoginLayoutBinding
import kotlinx.coroutines.launch

class LoginActivity: ComponentActivity() {

    private val viewModel: LoginViewModel by viewModels()
    private lateinit var binding: LoginLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = LoginLayoutBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.emailInput.doAfterTextChanged { editable ->
            viewModel.validateEmailOnChange(editable.toString())
        }

        binding.passwordInput.doAfterTextChanged { editable ->
            viewModel.validatePasswordOnChange(editable.toString())
        }

        binding.backBtn.setOnClickListener {
            //TODO: back btn
            if (binding.sendResetEmailBtn.isInvisible) finish()
            else changePasswordRecoveryViewVisibility(false)
        }

        binding.forgotPasswordBtn.setOnClickListener {
            changePasswordRecoveryViewVisibility(true)
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                launch {
                    viewModel.formState.collect { formState ->
                        binding.emailInputLayout.error = formState.emailError?.let { getString(it) }
                        binding.emailInputLayout.isErrorEnabled = formState.emailError != null

                        binding.passwordInputLayout.error = formState.passwordError?.let { getString(it)}
                        binding.passwordInputLayout.isErrorEnabled = formState.passwordError != null

                        binding.loginConfirmationBtn.isEnabled = formState.isAllValid

                        binding.sendResetEmailBtn.isEnabled = formState.emailError == null
                    }
                }
                launch {
                    viewModel.uiState.collect { state ->
                        when(state) {
                            is LoginViewModel.UIState.Loading -> showLoading(true)
                            is LoginViewModel.UIState.Success -> {
                                navigateToHome()
                            }
                            is LoginViewModel.UIState.Error -> {
                                showLoading(false)
                                Snackbar.make(binding.root, state.message, Snackbar.LENGTH_LONG).show()
                            }
                            is LoginViewModel.UIState.ResetEmailSuccess -> {
                                Snackbar.make(binding.root, getString(R.string.passResetSuccessSend) , Snackbar.LENGTH_SHORT).show()
                                showLoading(false)
                            }
                            else -> null
                        }
                    }
                }
            }

        }

        binding.loginConfirmationBtn.setOnClickListener {
            viewModel.logInUser(this)
        }

        binding.sendResetEmailBtn.setOnClickListener {
            viewModel.sendResetEmail(this)
        }
    }

    fun navigateToHome(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun showLoading(show: Boolean) {
        binding.loadingScreen.visibility = if (show) View.VISIBLE else View.GONE
    }

    /**
     * Changes login view to password recovery view by changing visibility of the elements
     * @param show if true - shows password recovery view and hides login view, if false - the other way around
     */
    fun changePasswordRecoveryViewVisibility (show: Boolean) {
        if (show) {
            binding.loginTitle.setText(getString(R.string.enter_email))
            binding.passwordInputLayout.visibility = View.INVISIBLE
            binding.forgotPasswordBtn.visibility = View.INVISIBLE
            binding.loginConfirmationBtn.visibility = View.INVISIBLE

            binding.sendResetEmailBtn.visibility = View.VISIBLE
        } else {
            binding.loginTitle.setText(getString(R.string.login))
            binding.passwordInputLayout.visibility = View.VISIBLE
            binding.forgotPasswordBtn.visibility = View.VISIBLE
            binding.loginConfirmationBtn.visibility = View.VISIBLE

            binding.sendResetEmailBtn.visibility = View.INVISIBLE
        }
    }
}