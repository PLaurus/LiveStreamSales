package tv.wfc.livestreamsales.features.settings.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import tv.wfc.livestreamsales.R
import tv.wfc.livestreamsales.databinding.FragmentSettingsBinding
import tv.wfc.livestreamsales.features.authorizeduser.ui.base.AuthorizedUserFragment
import tv.wfc.livestreamsales.features.settings.di.SettingsComponent
import tv.wfc.livestreamsales.features.settings.viewmodel.ISettingsViewModel
import javax.inject.Inject

class SettingsFragment: AuthorizedUserFragment(R.layout.fragment_settings) {
    private var viewBinding: FragmentSettingsBinding? = null

    private lateinit var settingsComponent: SettingsComponent

    @Inject
    override lateinit var viewModel: ISettingsViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        initializeSettingsComponent()
        injectDependencies()
    }

    override fun onContentViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onContentViewCreated(view, savedInstanceState)
        bindView(view)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unbindView()
    }

    private fun bindView(view: View){
        viewBinding = FragmentSettingsBinding.bind(view)
    }

    private fun unbindView(){
        viewBinding = null
    }

    private fun initializeSettingsComponent(){
        settingsComponent = authorizedUserComponent.settingsComponent().create(this)
    }

    private fun injectDependencies(){
        settingsComponent.inject(this)
    }
}