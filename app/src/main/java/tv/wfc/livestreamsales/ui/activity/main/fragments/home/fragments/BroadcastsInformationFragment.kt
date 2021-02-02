package tv.wfc.livestreamsales.ui.activity.main.fragments.home.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import tv.wfc.livestreamsales.R
import tv.wfc.livestreamsales.databinding.FragmentBroadcastsInformationBinding
import tv.wfc.livestreamsales.di.components.app.subscomponents.authorizeduser.subscomponents.main.subscomponents.home.subcomponents.broadcastsinformation.BroadcastsInformationComponent
import tv.wfc.livestreamsales.ui.activity.main.fragments.home.fragments.base.PropertyOfHomeFragment
import tv.wfc.livestreamsales.viewmodels.broadcastsinformation.IBroadcastsInformationViewModel
import javax.inject.Inject

class BroadcastsInformationFragment: PropertyOfHomeFragment(R.layout.fragment_broadcasts_information) {
    private var viewBinding: FragmentBroadcastsInformationBinding? = null

    private lateinit var broadcastsInformationComponent: BroadcastsInformationComponent

    @Inject
    override lateinit var viewModel: IBroadcastsInformationViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        initializeBroadcastsInformationComponent()
        injectDependencies()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindView(view)
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindView()
    }

    private fun bindView(view: View){
        viewBinding = FragmentBroadcastsInformationBinding.bind(view)
    }

    private fun unbindView(){
        viewBinding = null
    }

    private fun initializeBroadcastsInformationComponent(){
        broadcastsInformationComponent = homeComponent.broadcastsInformationComponent().create(this)
    }

    private fun injectDependencies(){
        broadcastsInformationComponent.inject(this)
    }
}