package com.khoavna.loacationreminders.ui.locations

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.khoavna.loacationreminders.databinding.FragmentLocationListBinding

class LocationListFragment : Fragment() {

    private val binding by lazy {
        FragmentLocationListBinding.inflate(layoutInflater)
    }

    private val viewModel: LocationListViewModel by viewModels()

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.locations.observe(viewLifecycleOwner) {
            binding.groupNoData.isVisible = it.isEmpty()
            binding.rvListLocation.isVisible = it.isNotEmpty()
        }
    }

    private fun showSettingSystem() {
        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).also {
            it.data = Uri.fromParts("package", requireActivity().packageName, null)
            startActivity(it)
        }
    }

    private fun checkPermission(permission: String): Boolean =
        ActivityCompat.checkSelfPermission(
            requireActivity(),
            permission
        ) == PackageManager.PERMISSION_DENIED

    private fun showAlert() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Permission Required")
            .setMessage("This Permission Is Required For Proper Working Of The App")
            .setNegativeButton("No") { d, _ ->
                d.dismiss()
                Toast.makeText(
                    requireContext(),
                    "This Permission Is Required For Proper Working Of The App",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .setPositiveButton("OK") { d, _ ->
                showSettingSystem()
                d.dismiss()
            }.show()
    }
}