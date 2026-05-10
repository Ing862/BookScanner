package com.ingwoj.bookscanner.userServices.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.ingwoj.bookscanner.R
import com.ingwoj.bookscanner.databinding.FragmentRegisterPasswordBinding
import kotlinx.coroutines.launch

class RegisterPasswordFragment : Fragment() {

    private var _binding: FragmentRegisterPasswordBinding? = null
    private val binding get() = _binding!!

    private val viewModel: UserRegistrationViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Validating input
        binding.passwordInput.doAfterTextChanged { editable ->
            viewModel.validatePasswordOnChange(editable.toString())
        }

        // Observe change in password, update UI
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.registrationFormState.collect { state ->
                    updateCondition(binding.passwordRequirement1, state.passwordConditions.minLength)
                    updateCondition(binding.passwordRequirement2, state.passwordConditions.hasUppercaseLetter)
                    updateCondition(binding.passwordRequirement3, state.passwordConditions.hasDigit)
                    updateCondition(binding.passwordRequirement4, state.passwordConditions.hasSpecialCharacter)
                }
            }
        }
    }

    // TODO: Add red and green text for password verification in registration (UI)
    // Updates UI
    fun updateCondition(text: TextView, conditionIsMet: Boolean){
        val icon = if (conditionIsMet) R.drawable.check_small_24px else R.drawable.check_indeterminate_small_24px
        text.setCompoundDrawablesWithIntrinsicBounds(icon, 0, 0, 0)
        text.compoundDrawablePadding = 8
    }

    override fun onPause() {
        super.onPause()
        viewModel.resetPasswordValidation()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}