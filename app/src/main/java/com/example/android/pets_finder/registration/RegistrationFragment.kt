package com.example.android.pets_finder.registration

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
import com.example.android.pets_finder.databinding.FragmentRegistrationBinding
import com.example.android.pets_finder.viewModelFactory.injectViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Fragment to allow the user to register in PetFinder.
 */
class RegistrationFragment : Fragment() {
    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!
    private lateinit var registrationViewModel: RegistrationViewModel

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity?.application as ApplicationContainer).getAppComponent()
            ?.plusRegistrationComponent()
            ?.inject(this)
        registrationViewModel = injectViewModel(factory)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registrationViewModel = ViewModelProvider(this)[RegistrationViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title =
            getString(R.string.registration_fragment_title)

        binding.apply {
            etSEmailAddress.addTextChangedListener {
                binding.emailLayout.error = EMPTY_ERROR
            }
            etContactName.addTextChangedListener {
                binding.usernameLayout.error = EMPTY_ERROR
            }
            etPhoneNumber.addTextChangedListener {
                binding.phoneLayout.error = EMPTY_ERROR
            }
            etPassword.addTextChangedListener {
                binding.passwordLayout.error = EMPTY_ERROR
            }
            etConfPassword.addTextChangedListener {
                binding.confPasswordLayout.error = EMPTY_ERROR
            }
        }

        binding.btnSigned.setOnClickListener {
            registrationViewModel.register(
                binding.etSEmailAddress.text.toString(),
                binding.etContactName.text.toString(),
                binding.etPhoneNumber.text.toString(),
                binding.etPassword.text.toString(),
                binding.etConfPassword.text.toString()
            )
        }

        binding.tvRedirectLogin.setOnClickListener {
            findNavController().navigate(R.id.action_registrationFragment_to_loginFragment)
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                registrationViewModel.userSignUpStatus.collect { uiState ->
                    registrationStatusChanged(uiState)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun registrationStatusChanged(uiState: RegistrationStatus) {
        if (uiState == RegistrationStatus.Loading) {
            setLoading()
            return
        } else {
            hideLoading()
        }

        when (uiState) {
            RegistrationStatus.EmptyEmail -> binding.emailLayout.error =
                getString(R.string.empty_email)
            RegistrationStatus.EmptyUserName -> binding.usernameLayout.error =
                getString(R.string.empty_user_name)
            RegistrationStatus.EmptyPhoneNumber -> binding.phoneLayout.error =
                getString(R.string.empty_phone_number)
            RegistrationStatus.EmptyPassword -> binding.passwordLayout.error =
                getString(R.string.empty_password)
            RegistrationStatus.EmptyConfirmPassword -> binding.confPasswordLayout.error =
                getString(R.string.empty_confirm_password)
            RegistrationStatus.PassAndConfDoNotMatch -> binding.confPasswordLayout.error =
                getString(R.string.password_and_confirm_password_do_not_match)
            RegistrationStatus.Error -> showSnackbar(R.string.register_error_generic)
            RegistrationStatus.Success -> {
                showSnackbar(R.string.successfully_registered)
                findNavController().navigate(
                    R.id.action_registrationFragment_to_advertisementListContainerFragment
                )
            }
        }
    }

    private fun showSnackbar(@StringRes id: Int) =
        Snackbar.make(binding.registrationLayout, id, Snackbar.LENGTH_LONG).show()

    private fun setLoading() {
        binding.progressBar.isVisible = true
        binding.btnSigned.text = ""
        binding.btnSigned.isEnabled = false
    }

    private fun hideLoading() {
        binding.progressBar.isVisible = true
        binding.btnSigned.text = getString(R.string.login)
        binding.btnSigned.isEnabled = true
    }

    companion object {
        const val EMPTY_ERROR = ""
    }
}
