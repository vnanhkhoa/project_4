package com.khoavna.loacationreminders.ui.locationdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.BundleCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.khoavna.loacationreminders.databinding.FragmentLocationDetailBinding
import com.khoavna.loacationreminders.ui.dto.LocationSelectDto
import org.koin.androidx.viewmodel.ext.android.viewModel

class LocationDetailFragment : Fragment() {

    companion object {
        const val LOCATION_SELECT_KEY = "LOCATION_SELECT_KEY"
    }

    private val viewModel: LocationDetailViewModel by viewModel()
    private val args: LocationDetailFragmentArgs by navArgs()

    private val binding by lazy {
        FragmentLocationDetailBinding.inflate(layoutInflater).also {
            it.viewModel = viewModel
            it.lifecycleOwner = requireActivity()
            it.executePendingBindings()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        args.location?.let {
            viewModel.apply {
                title.value = it.title
                description.value = it.description
                locationDto.value = LocationSelectDto(
                    name = it.locationName,
                    latitude = it.latitude,
                    longitude = it.longitude
                )
                location = it
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ) = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        findNavController().currentBackStackEntry?.let {
            it.savedStateHandle.getLiveData<Bundle>(LOCATION_SELECT_KEY)
                .observe(it) { result ->
                    val selectDto =
                        BundleCompat.getParcelable(result, "DATA", LocationSelectDto::class.java)
                    viewModel.locationDto.value = selectDto
                }
        }

        binding.apply {
            lnSelector.setOnClickListener {
                LocationDetailFragmentDirections.actionLocationDetailFragmentToMapFragment().also {
                    findNavController().navigate(it)
                }
            }

            btnSave.setOnClickListener {
                this@LocationDetailFragment.viewModel.saveLocation()
                Toast.makeText(requireContext(), "Save Success", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }
        }
    }

}