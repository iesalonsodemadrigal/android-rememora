package com.iesam.rememora.core.account.presentation

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.iesam.rememora.databinding.FragmentLoginBinding
import com.iesam.rememora.features.home.presentation.HomeActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
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
            }
        }
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
            val intent = Intent(activity, HomeActivity::class.java)
            startActivity(intent)
            //findNavController().navigate(LoginFragmentDirections.fromLoginToLogout())
        } else {
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}