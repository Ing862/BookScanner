package com.ingwoj.bookscanner.menuSections.homeSection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ingwoj.bookscanner.databinding.FragmentHomeBinding

import com.ingwoj.bookscanner.services.AuthService

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val auth: AuthService by lazy {
        AuthService()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TODO: check if works
        //val name = auth.getLoggedUser()
        //val name = ""
        //val message = getString(R.string.welcomeUser, name)

       // binding.homeWelcomeMessage.text = message
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}