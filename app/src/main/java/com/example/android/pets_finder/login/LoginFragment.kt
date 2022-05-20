package com.example.android.pets_finder.login

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
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.android.domain.common.Resource
import com.example.android.pets_finder.application.ApplicationContainer
import com.example.android.pets_finder.databinding.FragmentLoginBinding
import com.example.android.pets_finder.R
import com.example.android.pets_finder.viewModelFactory.injectViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var loginViewModel: LoginViewModel
    private var customProgressDialog: Dialog? = null

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
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                loginViewModel.userSignInStatus.collect { uiState ->
                    when (uiState) {
                        is Resource.Loading -> {
                            showProgressBar()
                        }
                        is Resource.Success -> {
                            customProgressDialog?.hide()
                            if (uiState.data != LoginViewModel.EMPTY_ID) {
                                Toast.makeText(
                                    requireContext(),
                                    R.string.successfully_logged_In,
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                                // если пользователь успешно авторизовался или уже был авторизован,
                                // то переход в фрагмент просмотра контактов
                                findNavController().navigate(
                                    R.id.action_loginFragment_to_advertisementListContainerFragment
                                )
                            }
                        }
                        is Resource.Error -> {
                            customProgressDialog?.hide()
                            Toast.makeText(requireContext(), uiState.message, Toast.LENGTH_SHORT)
                                .show()
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
