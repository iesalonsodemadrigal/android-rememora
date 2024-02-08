package com.iesam.rememora.core.account.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.android.material.snackbar.Snackbar
import com.iesam.rememora.R
import com.iesam.rememora.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<LoginViewModel>()

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract(),
    ) { res ->
        this.onSignInResult(res)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        setupView()
        return binding.root
    }

    private fun setupView() {
        binding.apply {
            buttonLogin.setOnClickListener {
                createSignInIntent()
            }
            buttonCancel.setOnClickListener {
                requireActivity().finish()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObserver()
        viewModel.checkSession()
    }

    private fun setupObserver() {
        val observer = Observer<LoginViewModel.UiState> { uiState ->
            if (uiState.isLoading) {
                //todo mostrar spinner
            } else {
                //ocultar spinner
                if (uiState.errorApp != null) {
                    //Mostrar error
                } else {
                    //Ocultar error
                    if (uiState.hasSession) {
                        navigateToHome()
                    }
                }
            }
        }
        viewModel.uiState.observe(viewLifecycleOwner, observer)
    }

    private fun createSignInIntent() {
        val providers = arrayListOf(
            AuthUI.IdpConfig.GoogleBuilder().build()
        )
        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build()
        signInLauncher.launch(signInIntent)
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            navigateToHome()
        } else {
            Snackbar.make(
                binding.root,
                getString(R.string.label_unknown_error),
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }

    private fun navigateToHome() {
        findNavController().navigate(LoginFragmentDirections.actionFromLoginToHome())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}