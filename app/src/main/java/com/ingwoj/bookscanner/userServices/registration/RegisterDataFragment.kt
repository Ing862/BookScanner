package com.ingwoj.bookscanner.userServices.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.ingwoj.bookscanner.databinding.FragmentRegisterDataBinding
import kotlinx.coroutines.launch

class RegisterDataFragment : Fragment() {

    private var _binding: FragmentRegisterDataBinding? = null
    private val binding get() = _binding!!

    private val viewModel: UserRegistrationViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterDataBinding.inflate(inflater, container, false)
        return binding.root
    }

    // TODO: FIX the registration error in user data input -> no errors displaying
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Validating input
        binding.nameInput.doAfterTextChanged { editable ->
            viewModel.validateNameOnChange(editable.toString())
        }
        binding.usernameInput.doAfterTextChanged { editable ->
            viewModel.validateUsernameOnChange(editable.toString())
        }
        binding.yearOfBirthInput.doAfterTextChanged { editable ->
            viewModel.validateYearOnChange(editable.toString())
        }
        binding.emailInput.doAfterTextChanged { editable ->
            viewModel.validateEmailOnChange(editable.toString())
        }

        // Input error display
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.registrationFormState.collect { registrationUiState ->
                    binding.nameInputLayout.error = registrationUiState.errors.nameError?.let { getString(it) }
                    binding.nameInputLayout.isErrorEnabled = registrationUiState.errors.nameError != null

                    binding.usernameInputLayout.error = registrationUiState.errors.usernameError?.let { getString(it) }
                    binding.usernameInputLayout.isErrorEnabled = registrationUiState.errors.usernameError != null

                    binding.yearOfBirthInputLayout.error = registrationUiState.errors.yearOfBirthError?.let { getString(it) }
                    binding.yearOfBirthInputLayout.isErrorEnabled = registrationUiState.errors.yearOfBirthError != null

                    binding.emailInputLayout.error = registrationUiState.errors.emailError?.let { getString(it) }
                    binding.emailInputLayout.isErrorEnabled = registrationUiState.errors.emailError != null
                }
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}