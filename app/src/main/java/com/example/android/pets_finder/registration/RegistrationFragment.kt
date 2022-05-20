package com.example.android.pets_finder.registration

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.android.domain.common.Resource
import com.example.android.pets_finder.R
import com.example.android.pets_finder.application.ApplicationContainer
import com.example.android.pets_finder.databinding.FragmentRegistrationBinding
import com.example.android.pets_finder.viewModelFactory.injectViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

class RegistrationFragment : Fragment() {
    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!
    private lateinit var registrationViewModel: RegistrationViewModel
    private var customProgressDialog: Dialog? = null

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
        // обработка нажатия на кнопку для регистрации
        binding.btnSigned.setOnClickListener {
            registrationViewModel.signUpUser(
                binding.etSEmailAddress.text.toString(),
                binding.etContactName.text.toString(),
                binding.etPhoneNumber.text.toString(),
                binding.etPassword.text.toString(),
                binding.etConfPassword.text.toString()
            )
        }
        // обработка нажатия на кнопку для перехода в фрагмент логина
        binding.tvRedirectLogin.setOnClickListener {
            findNavController().navigate(R.id.action_registrationFragment_to_loginFragment)
        }
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                registrationViewModel.userSignUpStatus.collect { uiState ->
                    when (uiState) {
                        is Resource.Loading -> {
                            showProgressBar()
                        }
                        is Resource.Success -> {
                            customProgressDialog?.hide()
                            if (uiState.data != RegistrationViewModel.EMPTY_ID) {
                                Toast.makeText(
                                    context,
                                    R.string.successfully_singed_up,
                                    Toast.LENGTH_SHORT
                                ).show()
                                findNavController().navigate(R.id.action_registrationFragment_to_advertisementListContainerFragment)
                            }
                        }
                        is Resource.Error -> {
                            customProgressDialog?.hide()
                            Toast.makeText(context, uiState.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        customProgressDialog = null
    }

    private fun showProgressBar() {
        customProgressDialog = Dialog(requireContext())
        customProgressDialog?.let {
            it.setContentView(R.layout.dialog_custom_progress)
            it.setCancelable(false)
            it.show()
        }
    }
}
