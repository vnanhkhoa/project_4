package com.khoavna.loacationreminders.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.khoavna.loacationreminders.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {
    private val viewModel: LoginViewModel by viewModels()
    private val binding by lazy {
        FragmentLoginBinding.inflate(layoutInflater)
    }

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) {

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.isLogin.observe(viewLifecycleOwner) {
            if (it) {
                LoginFragmentDirections.actionLoginFragmentToLocationListFragment().also { action ->
                    findNavController().navigate(action)
                }
            }
        }

        binding.btnLogin.setOnClickListener {
            signInLauncher.launch(signInIntent())
        }
    }

    private fun signInIntent() = AuthUI.getInstance()
        .createSignInIntentBuilder()
        .setAvailableProviders(
            listOf(
                AuthUI.IdpConfig.EmailBuilder().build(),
                AuthUI.IdpConfig.GoogleBuilder().build()
            )
        )
        .build()

}