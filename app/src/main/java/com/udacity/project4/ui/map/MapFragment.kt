package com.udacity.project4.ui.map

import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PointOfInterest
import com.udacity.project4.R
import com.udacity.project4.R.*
import com.udacity.project4.databinding.FragmentMapBinding
import com.udacity.project4.ui.dto.LocationSelectDto
import com.udacity.project4.ui.locationdetail.LocationDetailFragment
import com.udacity.project4.utils.PermissionUtil.checkPermission
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class MapFragment : Fragment(), MenuProvider {

    private val binding by lazy {
        FragmentMapBinding.inflate(layoutInflater)
    }

    private val viewModel: MapViewModel by viewModels()
    private var locationSelectDto: LocationSelectDto? = null
    private val args: MapFragmentArgs by navArgs()

    private lateinit var googleMap: GoogleMap
    private val fusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(requireContext())
    }

    private val menuHost: MenuHost by lazy {
        requireActivity()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        locationSelectDto = args.locationSelect
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ) = binding.root


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        menuHost.addMenuProvider(this)

        (childFragmentManager.findFragmentById(R.id.map_fragment) as? SupportMapFragment)?.run {
            getMapAsync {
                googleMap = it
                setMapEvents()
                updateLocation()
                viewModel.styleMap.observe(viewLifecycleOwner) { style ->
                    googleMap.mapType = style
                }
            }
        }

        binding.btnSave.setOnClickListener {
            findNavController().apply {
                previousBackStackEntry!!.savedStateHandle[LocationDetailFragment.LOCATION_SELECT_KEY] =
                    bundleOf("DATA" to viewModel.locationSelectDto)
                popBackStack()
            }
        }
    }

    private fun updateLocation() {
        if (!checkPermission()) {
            return
        }
        googleMap.isMyLocationEnabled = true
        locationSelectDto?.run {
            LatLng(latitude, longitude).also {
                addLocation(it)
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(it, 20f))
                return
            }
        }
        fusedLocationProviderClient.lastLocation.addOnSuccessListener {
            it?.run {
                LatLng(it.latitude, it.longitude).also { latLng ->
                    addLocation(latLng)
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20f))
                }
            }
        }
    }

    private fun setMapEvents() {
        googleMap.apply {
            setOnPoiClickListener {
                addMarker(MarkerOptions().position(it.latLng).title(it.name))?.let { marker ->
                    viewModel.updateMarkerAndPointOfInterest(marker, it)
                }
            }

            setOnMapClickListener { addLocation(it) }
        }
    }

    private fun addLocation(latLng: LatLng) {
        lifecycleScope.launch {
            val addresses = getAddress(latLng)
            if (addresses.isNullOrEmpty()) return@launch
            val address = addresses.first()
            PointOfInterest(latLng, "", address.getAddressLine(0)).let { pointOfInterest ->
                googleMap.addMarker(
                    MarkerOptions().position(pointOfInterest.latLng)
                        .title(pointOfInterest.name)
                )?.let { marker ->
                    viewModel.updateMarkerAndPointOfInterest(marker, pointOfInterest)
                }
            }
        }
    }

    private suspend fun getAddress(latLng: LatLng): List<Address>? = suspendCancellableCoroutine {
        Geocoder(requireContext()).apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                getFromLocation(
                    latLng.latitude, latLng.longitude, 1
                ) { addresses ->
                    it.resume(value = addresses)
                }
            } else {
                @Suppress("DEPRECATION") getFromLocation(
                    latLng.latitude, latLng.longitude, 1
                ).also { addresses ->
                    it.resume(value = addresses)
                }
            }
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.map_style, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        val style = when (menuItem.itemId) {
            R.id.normal -> GoogleMap.MAP_TYPE_NORMAL
            R.id.hybrid -> GoogleMap.MAP_TYPE_HYBRID
            R.id.satellite -> GoogleMap.MAP_TYPE_SATELLITE
            R.id.terrain -> GoogleMap.MAP_TYPE_TERRAIN
            else -> -1
        }

        return viewModel.updateMapStyle(style)
    }

    override fun onPause() {
        super.onPause()
        menuHost.removeMenuProvider(this)
    }
}