package com.khoavna.loacationreminders.ui.locations

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.auth.AuthUI
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.khoavna.loacationreminders.R
import com.khoavna.loacationreminders.databinding.FragmentLocationListBinding
import com.khoavna.loacationreminders.ui.locations.adapter.LocationAdapter
import com.khoavna.loacationreminders.utils.PermissionUtil.checkPermission
import org.koin.androidx.viewmodel.ext.android.viewModel

class LocationListFragment : Fragment(), MenuProvider {

    private val binding by lazy {
        FragmentLocationListBinding.inflate(layoutInflater)
    }

    private val viewModel: LocationListViewModel by viewModel()

    private val locationAdapter = LocationAdapter {
        LocationListFragmentDirections.actionLocationListFragmentToLocationDetailFragment(it)
            .let { action ->
                findNavController().navigate(action)
            }
    }


    private val activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            for (key in it.keys) {
                if (it[key] == false) {
                    showAlert()
                    return@registerForActivityResult
                }
            }

            viewModel.getLocation()
        }

    private val menuHost: MenuHost by lazy {
        requireActivity()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ) = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (FirebaseAuth.getInstance().currentUser == null) return

        menuHost.addMenuProvider(this, viewLifecycleOwner)

        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) || checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)) {
            activityResultLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        } else {
            viewModel.getLocation()
        }

        viewModel.locations.observe(viewLifecycleOwner) {
            binding.groupNoData.isVisible = it.isEmpty()
            binding.rvListLocation.isVisible = it.isNotEmpty()
            locationAdapter.submitList(it)
        }

        binding.apply {
            btnAdd.setOnClickListener {
                LocationListFragmentDirections.actionLocationListFragmentToLocationDetailFragment()
                    .let { action ->
                        findNavController().navigate(action)
                    }
            }

            rvListLocation.adapter = locationAdapter
            rvListLocation.layoutManager = LinearLayoutManager(requireContext())

        }
    }

    private fun showSettingSystem() {
        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).also {
            it.data = Uri.fromParts("package", requireActivity().packageName, null)
            startActivity(it)
        }
    }

    private fun showAlert() {
        MaterialAlertDialogBuilder(requireContext()).setTitle("Permission Required")
            .setMessage("This Permission Is Required For Proper Working Of The App")
            .setNegativeButton("No") { d, _ ->
                d.dismiss()
                Toast.makeText(
                    requireContext(),
                    "This Permission Is Required For Proper Working Of The App",
                    Toast.LENGTH_SHORT
                ).show()
            }.setPositiveButton("OK") { d, _ ->
                showSettingSystem()
                d.dismiss()
            }.show()
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.location_list, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        if (menuItem.itemId == R.id.logout) {
            AuthUI.getInstance().signOut(requireContext())
        }
        return false
    }

    override fun onStop() {
        super.onStop()
        menuHost.removeMenuProvider(this)
    }
}