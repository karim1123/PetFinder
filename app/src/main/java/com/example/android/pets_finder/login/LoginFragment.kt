package com.example.android.pets_finder.login

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.android.pets_finder.R
import com.example.android.pets_finder.application.ApplicationContainer
import com.example.android.pets_finder.databinding.FragmentLoginBinding
import com.example.android.pets_finder.viewModelFactory.injectViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var loginViewModel: LoginViewModel

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity?.application as ApplicationContainer).getAppComponent()
            ?.plusLoginComponent()
            ?.inject(this)
        loginViewModel = injectViewModel(factory)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title =
            getString(R.string.login_fragment_title)

        binding.etEmailAddress.addTextChangedListener { binding.usernameLayout.error = EMPTY_ERROR }
        binding.etPassword.addTextChangedListener { binding.passwordLayout.error = EMPTY_ERROR }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                loginViewModel.userSignInStatus.collect { uiState ->
                    loginStatusChanged(uiState)
                }
            }
        }

        // обработка нажатия кнопки для логина
        binding.btnLogin.setOnClickListener {
            loginViewModel.login(
                binding.etEmailAddress.text.toString(),
                binding.etPassword.text.toString()
            )
        }
        // переход в фрагмент регистрации
        binding.tvRedirectSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registrationFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun loginStatusChanged(uiState: LoginStatus) {
        if (uiState == LoginStatus.Loading) {
            setLoading()
            return
        } else {
            hideLoading()
        }

        when (uiState) {
            LoginStatus.EmptyEmail ->
                binding.usernameLayout.error = getString(R.string.empty_email)
            LoginStatus.EmptyPassword ->
                binding.passwordLayout.error = getString(R.string.empty_password)
            LoginStatus.Error -> showSnackbar(R.string.login_error_generic)
            LoginStatus.Success -> {
                showSnackbar(R.string.successfully_logged_In)
                findNavController().navigate(
                    R.id.action_loginFragment_to_advertisementListContainerFragment
                )
            }
        }
    }

    private fun showSnackbar(@StringRes id: Int) =
        Snackbar.make(binding.detailsLayout, id, Snackbar.LENGTH_LONG).show()

    private fun setLoading() {
        binding.progressBar.isVisible = true
        binding.btnLogin.text = ""
        binding.btnLogin.isEnabled = false
    }

    private fun hideLoading() {
        binding.progressBar.isVisible = true
        binding.btnLogin.text = getString(R.string.login)
        binding.btnLogin.isEnabled = true
    }

    companion object {
        const val EMPTY_ERROR = ""
    }
}
