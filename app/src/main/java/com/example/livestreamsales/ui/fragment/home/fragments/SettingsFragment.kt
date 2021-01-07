package com.example.livestreamsales.ui.fragment.home.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.livestreamsales.R
import com.example.livestreamsales.databinding.FragmentSettingsBinding

class SettingsFragment: Fragment(R.layout.fragment_settings) {

    private var viewBinding: FragmentSettingsBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding = FragmentSettingsBinding.bind(view)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewBinding = null
    }
}