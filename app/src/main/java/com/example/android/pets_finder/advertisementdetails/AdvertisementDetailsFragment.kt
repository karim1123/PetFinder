package com.example.android.pets_finder.advertisementdetails

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.android.domain.common.AdvertisementDetailsUiState
import com.example.android.domain.entities.UserModel
import com.example.android.pets_finder.R
import com.example.android.pets_finder.application.ApplicationContainer
import com.example.android.pets_finder.databinding.FragmentAdvertisementDetailsBinding
import com.example.android.pets_finder.viewModelFactory.injectViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import javax.inject.Inject

class AdvertisementDetailsFragment : Fragment() {
    private var _binding: FragmentAdvertisementDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var advertisementDetailsViewModel: AdvertisementDetailsViewModel
    private val args: AdvertisementDetailsFragmentArgs by navArgs()
    private val adapter = AdvertisementDetailsAdapter()

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity?.application as ApplicationContainer).getAppComponent()
            ?.plusAdvertisementDetailsComponent()
            ?.inject(this)
        advertisementDetailsViewModel = injectViewModel(factory)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        advertisementDetailsViewModel =
            ViewModelProvider(this)[AdvertisementDetailsViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdvertisementDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title =
            getString(R.string.advertisement_details_fragment_title)

        advertisementDetailsViewModel.onPostsValuesChange(args.advertisementData.userId)

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                advertisementDetailsViewModel.advertisementDetailsStatus.collect { uiState ->
                    when (uiState) {
                        is AdvertisementDetailsUiState.Loading ->
                            binding.progressBar.isVisible = true
                        is AdvertisementDetailsUiState.Success -> {
                            binding.progressBar.isVisible = false
                            if (uiState.user != null) {
                                renderAdvertisementData()
                                uiState.user?.let { renderUserData(it) }
                            }
                            if (uiState.deleteFlag) {
                                Toast.makeText(
                                    requireContext(),
                                    R.string.advertisement_removed,
                                    Toast.LENGTH_SHORT
                                ).show()
                                findNavController().navigate(
                                    R.id.action_advertisementDetailsFragment_to_advertisementListContainerFragment
                                )
                            }
                        }
                        is AdvertisementDetailsUiState.Error -> {
                            binding.progressBar.isVisible = false
                            Toast.makeText(
                                requireContext(),
                                uiState.exception,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
        if (advertisementDetailsViewModel.isUserAdvertisementAuthor()) {
            requireActivity().invalidateOptionsMenu()
            menu.findItem(R.id.action_delete_advertisement).isVisible = true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                true
            }
            R.id.action_logout -> {
                Firebase.auth.signOut()
                findNavController().navigate(
                    R.id.action_advertisementDetailsFragment_to_loginFragment
                )
                true
            }
            R.id.action_delete_advertisement -> {
                showDeleteAdvertisementDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onStop() {
        advertisementDetailsViewModel.removePostsValuesChangesListener()
        super.onStop()
    }

    override fun onDestroyView() {
        binding.imagesRecycler.adapter = null
        _binding = null
        super.onDestroyView()
    }

    private fun renderAdvertisementData() {
        binding.tvAddress.text = args.advertisementData.address
        binding.tvDescription.text = args.advertisementData.description
        adapter.addSelectedImagesUris(args.advertisementData.urisList)
        binding.imagesRecycler.adapter = adapter
    }

    private fun renderUserData(user: UserModel) {
        binding.tvName.text = user.userName
        binding.tvPhone.text = user.userPhoneNumber
        binding.tvEmail.text = user.userEmail
    }

    private fun showDeleteAdvertisementDialog() {
        val alertDialog: AlertDialog? = activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setTitle(R.string.delete_advertisement_title)
                setPositiveButton(
                    R.string.ok
                ) { _, _ ->
                    advertisementDetailsViewModel.deleteAdvertisement(
                        args.advertisementData.advertisementId
                    )
                }
                setNegativeButton(
                    R.string.cancel
                ) { _, _ ->
                }
            }
            builder.create()
        }
        alertDialog?.show()
    }
}
