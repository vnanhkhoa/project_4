package com.khoavna.loacationreminders.ui.locationdetail

import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.BundleCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.khoavna.loacationreminders.databinding.FragmentLocationDetailBinding
import com.khoavna.loacationreminders.receiver.LocationReceiver
import com.khoavna.loacationreminders.ui.dto.LocationSelectDto
import com.khoavna.loacationreminders.utils.Constants
import com.khoavna.loacationreminders.utils.PermissionUtil.checkPermission
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class LocationDetailFragment : Fragment() {

    companion object {
        const val LOCATION_SELECT_KEY = "LOCATION_SELECT_KEY"
    }

    private val intentLocation by lazy {
        Intent(activity, LocationReceiver::class.java).run {
            action = Constants.ACTION_LOCATION_REMINDER
            PendingIntent.getBroadcast(
                requireActivity(), 0, this, PendingIntent.FLAG_IMMUTABLE
            )
        }
    }

    private val geofenceClient by lazy {
        LocationServices.getGeofencingClient(requireActivity())
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
                    name = it.locationName, latitude = it.latitude, longitude = it.longitude
                )
                setLocation(it)
                setUpdate(true)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ) = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        findNavController().currentBackStackEntry?.let {
            it.savedStateHandle.getLiveData<Bundle>(LOCATION_SELECT_KEY).observe(it) { result ->
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
                addLocationReminder()
            }
        }
    }

    private fun addLocationReminder() {
        lifecycleScope.launch {
            viewModel.apply {
                if (!checkPermission()) return@apply
                saveLocation {
                    val geofence = createGeofence(it)
                    val request = createGeofencingRequest(geofence = geofence)
                    binding.viewLoading.loadingScreen.isVisible = true
                    geofenceClient.addGeofences(request, intentLocation).run {
                        addOnSuccessListener {
                            binding.viewLoading.loadingScreen.isVisible = false
                            Toast.makeText(requireContext(), "Save Success", Toast.LENGTH_SHORT)
                                .show()
                            findNavController().popBackStack()
                        }
                        addOnFailureListener {
                            deleteLocation()
                            binding.viewLoading.loadingScreen.isVisible = false
                            Toast.makeText(
                                requireContext(), "Add location reminder error", Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }

            }
        }
    }

    private fun createGeofencingRequest(geofence: Geofence) =
        GeofencingRequest.Builder().setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            .addGeofence(geofence).build()

}
