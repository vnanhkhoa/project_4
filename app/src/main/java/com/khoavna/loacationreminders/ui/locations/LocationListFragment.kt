package com.khoavna.loacationreminders.ui.locations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.khoavna.loacationreminders.databinding.FragmentLocationListBinding

class LocationListFragment : Fragment() {

    private val binding by lazy {
        FragmentLocationListBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root

}