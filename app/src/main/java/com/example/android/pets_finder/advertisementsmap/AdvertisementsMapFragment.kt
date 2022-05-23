package com.example.android.pets_finder.advertisementsmap

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.android.domain.common.LatestAdvertisementListUiState
import com.example.android.domain.entities.AdvertisementModel
import com.example.android.pets_finder.R
import com.example.android.pets_finder.application.ApplicationContainer
import com.example.android.pets_finder.databinding.FragmentAdvertisementsMapBinding
import com.example.android.pets_finder.utils.MapperUtils.mapToParcelize
import com.example.android.pets_finder.viewModelFactory.injectViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import javax.inject.Inject

class AdvertisementsMapFragment : Fragment(), OnMapReadyCallback,
    GoogleMap.OnInfoWindowClickListener {
    private var _binding: FragmentAdvertisementsMapBinding? = null
    private val binding get() = _binding!!
    private lateinit var advertisementMapViewModel: AdvertisementMapViewModel
    private var map: GoogleMap? = null

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity?.application as ApplicationContainer).getAppComponent()
            ?.plusAdvertisementMapComponent()
            ?.inject(this)
        advertisementMapViewModel = injectViewModel(factory)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        advertisementMapViewModel =
            ViewModelProvider(this)[AdvertisementMapViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdvertisementsMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title =
            getString(R.string.advertisement_map_fragment_title)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
        advertisementMapViewModel.onPostsValuesChange()
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                advertisementMapViewModel.advertisementListStatus.collect { uiState ->
                    when (uiState) {
                        is LatestAdvertisementListUiState.Loading ->
                            binding.progressBar.isVisible = true
                        is LatestAdvertisementListUiState.Success -> {
                            binding.progressBar.isVisible = false
                            drawAllPins(uiState.advertisements)
                        }
                        is LatestAdvertisementListUiState.Error -> {
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

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map?.setOnInfoWindowClickListener(this)
    }

    // по нажатию на окно с информацией пина
    // происходит переход в детальную информацию объявления
    override fun onInfoWindowClick(marker: Marker) {
        val selectedAdvertisement: AdvertisementModel = marker.tag as AdvertisementModel
        val action =
            AdvertisementsMapFragmentDirections.actionAdvertisementsMapFragmentToAdvertisementDetailsFragment(
                selectedAdvertisement.mapToParcelize()
            )
        findNavController().navigate(action)
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
                    R.id.action_advertisementsMapFragment_to_loginFragment
                )
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onStop() {
        advertisementMapViewModel.removePostsValuesChangesListener()
        super.onStop()
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    private fun drawAllPins(advertisements: List<AdvertisementModel>) {
        val latLngBuilder = LatLngBounds.Builder()
        advertisements.forEach { advertisement ->
            val latitude = advertisement.latitude
            val longitude = advertisement.longitude
            if (latitude != null && longitude != null) {
                latLngBuilder.include(LatLng(latitude, longitude))
                lifecycleScope.launch {
                    Glide.with(this@AdvertisementsMapFragment)
                        .asBitmap()
                        .load(advertisement.urisList[0])
                        .circleCrop()
                        .into(object : CustomTarget<Bitmap>(ICON_SIZE, ICON_SIZE) {
                            override fun onResourceReady(
                                resource: Bitmap,
                                transition: Transition<in Bitmap>?
                            ) {
                                val bitmap = BitmapDescriptorFactory.fromBitmap(resource)
                                map?.addMarker(
                                    MarkerOptions()
                                        .position(LatLng(latitude, longitude))
                                        .title(
                                            getString(
                                                R.string.advertisement_list_item_title,
                                                advertisement.petStatus,
                                                advertisement.petType
                                            )
                                        )
                                        .snippet(advertisement.address)
                                        .icon(bitmap)
                                )?.tag = advertisement
                            }

                            override fun onLoadCleared(placeholder: Drawable?) {
                            }
                        })
                }
            }
        }
        val latLngBound = latLngBuilder.build()
        // карта приблизится до уровя, когда видны все пины
        map?.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBound, PADDING))
    }

    companion object {
        const val ICON_SIZE = 150
        const val PADDING = 100
    }
}
