package com.iesam.rememora.core.account.presentation

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.iesam.rememora.app.domain.ErrorApp
import com.iesam.rememora.databinding.FragmentLogoutBinding
import com.iesam.rememora.features.home.presentation.HomeActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LogoutFragment : Fragment() {
    private var _binding: FragmentLogoutBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<LogoutViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLogoutBinding.inflate(inflater, container, false)
        setupView()
        return binding.root
    }

    private fun setupView() {
        binding.buttonLogout.setOnClickListener {
            viewModel.logout()
        }
        binding.buttonDeleteAccount.setOnClickListener {
            viewModel.deleteAccount()
        }
    }

    private fun init() {
        val intent = Intent(activity, HomeActivity::class.java)
        startActivity(intent)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObserver()
    }

    private fun setupObserver() {
        val observer = Observer<LogoutViewModel.UiState> {
            if (it.isLoading) {
            } else {
                if (it.errorApp != null) {
                    showError(it.errorApp)
                } else {
                    if (it.isOk) {
                        init()
                    }
                }
            }
        }
        viewModel.uiState.observe(viewLifecycleOwner, observer)
    }

    private fun showError(error: ErrorApp) {
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}