package com.ingwoj.bookscanner.menuSections.userProfileSection

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.ingwoj.bookscanner.MainActivity
import com.ingwoj.bookscanner.R
import com.ingwoj.bookscanner.databinding.FragmentLoggedInUserProfileBinding
import kotlin.getValue

class UserProfileFragment : Fragment() {

    private var _binding: FragmentLoggedInUserProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: UserProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentLoggedInUserProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //binding.profileUsername.setText()

        val settingsMenuButton = binding.settingsBtn

        settingsMenuButton.setOnClickListener { button ->
            val popup = PopupMenu(requireContext(), button)
            popup.menuInflater.inflate(R.menu.user_menu, popup.menu)

            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.option_settings -> {
                        true
                    }
                    R.id.option_log_out -> {
                        // TODO: poprawić na poprawne
                        viewModel.logOut()
                        val intent = Intent(requireActivity(), MainActivity::class.java)
                        startActivity(intent)
                        requireActivity().finish()
                        true
                    }
                    else -> false
                }

            }

            popup.show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}