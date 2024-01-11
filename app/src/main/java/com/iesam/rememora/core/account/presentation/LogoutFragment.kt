package com.iesam.rememora.core.account.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.iesam.rememora.app.domain.ErrorApp
import com.iesam.rememora.app.extensions.hide
import com.iesam.rememora.app.extensions.show
import com.iesam.rememora.databinding.FragmentLogoutBinding
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
        binding.apply {
            buttonLogout.setOnClickListener {
                viewModel.logout()
            }
            buttonDeleteAccount.setOnClickListener {
                viewModel.deleteAccount()
            }
        }
    }

    private fun init() {
        findNavController().navigate(LogoutFragmentDirections.actionFromLogoutToLogin())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObserver()
    }

    private fun setupObserver() {
        val observer = Observer<LogoutViewModel.UiState> {
            if (it.isLoading) {
                binding.loading.root.show()
            } else {
                if (it.errorApp != null) {
                    binding.loading.root.hide()
                    showError(it.errorApp)
                } else {
                    if (it.isOk) {
                        binding.loading.root.hide()
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