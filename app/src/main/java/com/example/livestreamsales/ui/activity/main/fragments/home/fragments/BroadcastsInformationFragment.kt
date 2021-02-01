package com.example.livestreamsales.ui.activity.main.fragments.home.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.livestreamsales.R
import com.example.livestreamsales.databinding.FragmentBroadcastsInformationBinding

class BroadcastsInformationFragment: Fragment(R.layout.fragment_broadcasts_information) {

    private var viewBinding: FragmentBroadcastsInformationBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding = FragmentBroadcastsInformationBinding.bind(view)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewBinding = null
    }
}