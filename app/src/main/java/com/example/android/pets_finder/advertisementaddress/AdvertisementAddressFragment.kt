package com.example.android.pets_finder.advertisementaddress

import android.content.Context
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.android.pets_finder.R
import com.example.android.pets_finder.application.ApplicationContainer
import com.example.android.pets_finder.databinding.FragmentAdvertisementAddressBinding
import com.example.android.pets_finder.utils.GetAddressUtil.getContactAddress
import com.example.android.pets_finder.viewModelFactory.injectViewModel
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.Locale
import javax.inject.Inject

class AdvertisementAddressFragment : Fragment(), OnMapReadyCallback {
    private var _binding: FragmentAdvertisementAddressBinding? = null
    private val binding get() = _binding!!
    private lateinit var advertisementAddressViewModel: AdvertisementAddressViewModel
    private var map: GoogleMap? = null
    private val args: AdvertisementAddressFragmentArgs by navArgs()

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity?.application as ApplicationContainer).getAppComponent()
            ?.plusAdvertisementAddressComponent()
            ?.inject(this)
        advertisementAddressViewModel = injectViewModel(factory)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        advertisementAddressViewModel =
            ViewModelProvider(this)[AdvertisementAddressViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdvertisementAddressBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title =
            getString(R.string.advertisement_address_fragment_title)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
        if (args.advertisementData != null) {
            advertisementAddressViewModel.advertisement.value = args.advertisementData!!
        }
        binding.btnSelectLocation.setOnClickListener {
            if (advertisementAddressViewModel.advertisement.value.address.isNotBlank()) {
                val action =
                    AdvertisementAddressFragmentDirections.actionAdvertisementAddressFragmentToCreateAdFragment(
                        advertisementAddressViewModel.advertisement.value
                    )
                findNavController().navigate(action)
            } else Toast.makeText(
                requireContext(),
                R.string.address_not_selected,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map?.let { setMapLongClick(it) }
    }

    private fun setMapLongClick(map: GoogleMap) {
        map.setOnMapLongClickListener { latLng ->
            map.clear()
            map.addMarker(
                MarkerOptions()
                    .position(latLng)
            )
            val selectedAddress = getSelectedPinAddress(latLng)
            binding.addressEditText.setText(selectedAddress)
            advertisementAddressViewModel.advertisement.value.apply {
                address = selectedAddress
                latitude = latLng.latitude
                longitude = latLng.longitude
            }
        }
    }

    private fun getSelectedPinAddress(latLng: LatLng): String {
        val geocoder = Geocoder(context, Locale.getDefault())
        return geocoder.getContactAddress(
            latitude = latLng.latitude,
            longitude = latLng.longitude
        )
    }
}
