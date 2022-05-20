package com.example.android.pets_finder.createadvertisement

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.android.domain.common.CreateAdvertisementUiState
import com.example.android.domain.entities.AdvertisementModel
import com.example.android.pets_finder.R
import com.example.android.pets_finder.application.ApplicationContainer
import com.example.android.pets_finder.databinding.FragmentCreateAdBinding
import com.example.android.pets_finder.utils.AdvertisementPetStatuses
import com.example.android.pets_finder.utils.AdvertisementPetTypes
import com.example.android.pets_finder.viewModelFactory.injectViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import javax.inject.Inject
import kotlinx.coroutines.launch

class CreateAdvertisementFragment : Fragment(), ActivityCompat.OnRequestPermissionsResultCallback {
    private var _binding: FragmentCreateAdBinding? = null
    private val binding get() = _binding!!
    private lateinit var createAdvertisementViewModel: CreateAdvertisementViewModel
    private var customProgressDialog: Dialog? = null
    private val adapter = CreateAdvertisementImagesListItemAdapter()

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                // получение картинок из хранилища устройства
                getImagesFromExternalStorage(selectImagesActivityResult)
            } else {
                Snackbar.make(
                    binding.root,
                    R.string.storage_permission_denied,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

    private val selectImagesActivityResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                if (data?.clipData != null) {
                    val count = data.clipData?.itemCount ?: 0
                    for (i in 0 until count) {
                        val imageUri: Uri? = data.clipData?.getItemAt(i)?.uri
                        imageUri?.let {
                            createAdvertisementViewModel.imagesUris.add(it.toString())
                        }
                    }
                } else if (data?.data != null) {
                    val imageUri: Uri? = data.data
                    imageUri?.let { createAdvertisementViewModel.imagesUris.add(it.toString()) }
                }
                adapter.addSelectedImagesUris(createAdvertisementViewModel.imagesUris)
                binding.imagesRecycler.isVisible = true
            }
        }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity?.application as ApplicationContainer).getAppComponent()
            ?.plusCreateAdvertisementComponent()
            ?.inject(this)
        createAdvertisementViewModel = injectViewModel(factory)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        createAdvertisementViewModel =
            ViewModelProvider(this)[CreateAdvertisementViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateAdBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title =
            getString(R.string.create_advertisement_fragment_title)
        binding.imagesRecycler.adapter = adapter
        createAdvertisementViewModel.onPostsValuesChange()
        // обработка нажатия на кнопку выбора картинок
        binding.btnSelectImages.setOnClickListener { showStoragePreview() }
        // обработка нажатия по чек боксам
        onCheckboxClicked(
            binding.cbCat,
            binding.cbDog,
            binding.cbMissingPet,
            binding.cbFoundPet,
            binding.cbHomelessPet
        )
        // обработка нажатия на кнопку сохранения уведомления
        binding.btnSaveAd.setOnClickListener {
            createAdvertisementViewModel.createAdvertisement(
                address = binding.etAddress.text.toString()
            )
        }
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                createAdvertisementViewModel.advertisementListStatus.collect { uiStatus ->
                    when (uiStatus) {
                        is CreateAdvertisementUiState.Loading -> showProgressBar()
                        is CreateAdvertisementUiState.Success -> {
                            if (uiStatus.user != null) {
                                customProgressDialog?.hide()
                                createAdvertisementViewModel.advertisement = AdvertisementModel(
                                    userId = uiStatus.user!!.id,
                                    userEmail = uiStatus.user!!.userEmail,
                                    userPhoneNumber = uiStatus.user!!.userPhoneNumber,
                                    userName = uiStatus.user!!.userName
                                )
                            }
                            if (uiStatus.advertisement != null && uiStatus.advertisement != null) {
                                createAdvertisementViewModel.addAdvertisementToBd(uiStatus.advertisement!!)
                            }
                            if (uiStatus.closeFlag) {
                                customProgressDialog?.hide()
                                Toast.makeText(
                                    requireContext(),
                                    R.string.advertisement_created,
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                                findNavController().navigate(
                                    R.id.action_createAdFragment_to_advertisementListContainerFragment
                                )
                            }
                        }
                        is CreateAdvertisementUiState.Error -> {
                            customProgressDialog?.hide()
                            Toast.makeText(requireContext(), uiStatus.exception, Toast.LENGTH_SHORT)
                                .show()
                            if (uiStatus.exception == CreateAdvertisementViewModel.AUTHORIZATION_ERROR) {
                                findNavController().navigate(
                                    R.id.action_createAdFragment_to_advertisementListContainerFragment
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                true
            }
            R.id.action_logout -> {
                Firebase.auth.signOut()
                findNavController().navigate(
                    R.id.action_createAdFragment_to_loginFragment
                )
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        _binding = null
        customProgressDialog = null
        createAdvertisementViewModel.removePostsValuesChangesListener()
        super.onDestroy()
    }

    private fun showStoragePreview() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            getImagesFromExternalStorage(selectImagesActivityResult)
        } else requestStoragePermission()
    }

    private fun requestStoragePermission() {
        if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Snackbar.make(
                binding.root,
                R.string.storage_access_required,
                Snackbar.LENGTH_INDEFINITE
            ).setAction(R.string.ok) {
                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }.show()
        } else {
            Snackbar.make(
                binding.root,
                R.string.storage_permission_not_available,
                Snackbar.LENGTH_INDEFINITE
            ).setAction(R.string.ok) {
                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }.show()
        }
    }

    private fun getImagesFromExternalStorage(selectImagesActivityResult: ActivityResultLauncher<Intent>) {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.type = IMAGE_CONTENT_TYPE
        selectImagesActivityResult.launch(intent)
    }

    private fun onCheckboxClicked(vararg views: View) {
        for (view in views) {
            view.setOnClickListener {
                changeCheckBoxStatus(view)
            }
        }
    }

    // метод для проверки того, что выбран только один чек бокс из категории
    private fun changeCheckBoxStatus(view: View) {
        if (view is CheckBox) {
            val checked: Boolean = view.isChecked
            when (view.id) {
                binding.cbCat.id -> {
                    if (checked) {
                        binding.cbDog.isChecked = false
                        createAdvertisementViewModel.petType = AdvertisementPetTypes.cat.name
                    }
                }
                binding.cbDog.id -> {
                    if (checked) {
                        binding.cbCat.isChecked = false
                        createAdvertisementViewModel.petType = AdvertisementPetTypes.dog.name
                    }
                }
                binding.cbMissingPet.id -> {
                    if (checked) {
                        binding.cbFoundPet.isChecked = false
                        binding.cbHomelessPet.isChecked = false
                        createAdvertisementViewModel.petStatus =
                            AdvertisementPetStatuses.missed.name
                    }
                }
                binding.cbFoundPet.id -> {
                    if (checked) {
                        binding.cbMissingPet.isChecked = false
                        binding.cbHomelessPet.isChecked = false
                        createAdvertisementViewModel.petStatus = AdvertisementPetStatuses.found.name
                    }
                }
                binding.cbHomelessPet.id -> {
                    if (checked) {
                        binding.cbMissingPet.isChecked = false
                        binding.cbFoundPet.isChecked = false
                        createAdvertisementViewModel.petStatus =
                            AdvertisementPetStatuses.homeless.name
                    }
                }
            }
        }
    }

    private fun showProgressBar() {
        customProgressDialog = Dialog(requireContext())
        customProgressDialog?.let {
            it.setContentView(R.layout.dialog_custom_progress)
            it.setCancelable(false)
            it.show()
        }
    }

    companion object {
        const val IMAGE_CONTENT_TYPE = "image/*"
    }
}