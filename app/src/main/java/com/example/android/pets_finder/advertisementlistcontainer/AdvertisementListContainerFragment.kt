package com.example.android.pets_finder.advertisementlistcontainer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.android.pets_finder.R
import com.example.android.pets_finder.databinding.FragmentAdvertisementListContainerBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class AdvertisementListContainerFragment : Fragment() {
    private lateinit var advertisementTabsListNamesArray: Array<String>
    private var _binding: FragmentAdvertisementListContainerBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdvertisementListContainerBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title =
            getString(R.string.advertisement_list_fragment_title)
        advertisementTabsListNamesArray =
            resources.getStringArray(R.array.advertisement_list_tabs_name)
        val advertisementListViewPagerAdapter = AdvertisementListViewPagerAdapter(
            this,
            advertisementTabsListNamesArray.size
        )
        // переход в фрагмент создания объявления
        binding.navigateToCreateAdvertisementFragment.setOnClickListener {
            findNavController().navigate(
                R.id.action_advertisementListContainerFragment_to_createAdFragment
            )
        }
        binding.AdvertisementListViewPager.adapter = advertisementListViewPagerAdapter
        TabLayoutMediator(binding.tabLayout, binding.AdvertisementListViewPager) { tab, position ->
            tab.text = advertisementTabsListNamesArray[position]
        }.attach()
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
                    R.id.action_advertisementListContainerFragment_to_loginFragment
                )
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}
