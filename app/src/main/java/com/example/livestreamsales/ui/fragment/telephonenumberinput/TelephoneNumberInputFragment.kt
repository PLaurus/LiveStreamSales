package com.example.livestreamsales.ui.fragment.telephonenumberinput

import android.os.Bundle
import android.view.View
import com.example.livestreamsales.R
import com.example.livestreamsales.databinding.FragmentTelephoneNumberInputBinding
import com.example.livestreamsales.ui.fragment.base.ChildFragmentOfMainActivity

class TelephoneNumberInputFragment: ChildFragmentOfMainActivity(R.layout.fragment_telephone_number_input) {

    private var viewBinding: FragmentTelephoneNumberInputBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding = FragmentTelephoneNumberInputBinding.bind(view)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewBinding = null
    }
}