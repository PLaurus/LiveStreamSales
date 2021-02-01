package com.example.livestreamsales.ui.activity.main.fragments.home

import android.os.Bundle
import android.view.View
import com.example.livestreamsales.R
import com.example.livestreamsales.databinding.FragmentHomeBinding
import com.example.livestreamsales.ui.activity.main.fragments.base.MainFragment

class HomeFragment: MainFragment(R.layout.fragment_home) {
    private var viewBinding: FragmentHomeBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding = FragmentHomeBinding.bind(view)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewBinding = null
    }
}