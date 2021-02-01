package tv.wfc.livestreamsales.ui.activity.main.fragments.home.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import tv.wfc.livestreamsales.R
import tv.wfc.livestreamsales.databinding.FragmentSettingsBinding

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