package com.example.android.pets_finder.advertisementlist

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.domain.common.LatestAdvertisementListUiState
import com.example.android.domain.entities.AdvertisementModel
import com.example.android.pets_finder.advertisementlist.recycler.AdvertisementListItemAdapter
import com.example.android.pets_finder.advertisementlistcontainer.AdvertisementListContainerFragmentDirections
import com.example.android.pets_finder.application.ApplicationContainer
import com.example.android.pets_finder.databinding.FragmentAdvertisementListBinding
import com.example.android.pets_finder.utils.AdvertisementPetStatuses
import com.example.android.pets_finder.utils.MapperUtils.mapToParcelize
import com.example.android.pets_finder.viewModelFactory.injectViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class AdvertisementListFragment : Fragment() {
    private var _binding: FragmentAdvertisementListBinding? = null
    private val binding get() = _binding!!
    private lateinit var advertisementListViewModel: AdvertisementListViewModel

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity?.application as ApplicationContainer).getAppComponent()
            ?.plusAdvertisementListComponent()
            ?.inject(this)
        advertisementListViewModel = injectViewModel(factory)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        advertisementListViewModel =
            ViewModelProvider(this)[AdvertisementListViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdvertisementListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        advertisementListViewModel.onPostsValuesChange()
        preparingAdvertisementsInfoForDisplay()
    }

    override fun onStop() {
        advertisementListViewModel.removePostsValuesChangesListener()
        super.onStop()
    }

    override fun onDestroyView() {
        binding.advertisementListRecycler.adapter = null
        _binding = null
        super.onDestroyView()
    }

    private fun preparingAdvertisementsInfoForDisplay() {
        // получаем номер выбранного "таба"
        val tabPosition = requireArguments().getInt(ARG_POSITION)
        val advertisementAdapter =
            AdvertisementListItemAdapter { petModel -> adapterOnClick(petModel) }
        val advertisementListRecyclerView = binding.advertisementListRecycler
        advertisementListRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        advertisementListRecyclerView.adapter = advertisementAdapter
        advertisementListRecyclerView.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                VERTICAL
            )
        )
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                advertisementListViewModel.advertisementListStatus.collect { uiState ->
                    when (uiState) {
                        is LatestAdvertisementListUiState.Loading ->
                            binding.progressBar.isVisible = true
                        is LatestAdvertisementListUiState.Success -> {
                            binding.progressBar.isVisible = false
                            // в зависимости от выбранного "таба" фильтруются объвления
                            when (tabPosition) {
                                0 -> advertisementAdapter.submitList(uiState.advertisements
                                    .filter { it.petStatus == AdvertisementPetStatuses.missed.name })
                                1 -> advertisementAdapter.submitList(uiState.advertisements
                                    .filter { it.petStatus == AdvertisementPetStatuses.found.name })
                                2 -> advertisementAdapter.submitList(uiState.advertisements
                                    .filter { it.petStatus == AdvertisementPetStatuses.homeless.name })
                            }
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

    private fun adapterOnClick(advertisementModel: AdvertisementModel) {
        val action =
            AdvertisementListContainerFragmentDirections.actionAdvertisementListContainerFragmentToAdvertisementDetailsFragment(
                advertisementModel.mapToParcelize()
            )
        findNavController().navigate(action)
    }

    companion object {
        const val ARG_POSITION = "position"

        fun getInstance(position: Int): Fragment {
            val advertisementListFragment = AdvertisementListFragment()
            val bundle = Bundle()
            bundle.putInt(ARG_POSITION, position)
            advertisementListFragment.arguments = bundle
            return advertisementListFragment
        }
    }
}
