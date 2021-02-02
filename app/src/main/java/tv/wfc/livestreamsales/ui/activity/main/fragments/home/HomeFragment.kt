package tv.wfc.livestreamsales.ui.activity.main.fragments.home

import android.content.Context
import android.os.Bundle
import android.view.View
import tv.wfc.livestreamsales.R
import tv.wfc.livestreamsales.databinding.FragmentHomeBinding
import tv.wfc.livestreamsales.di.components.app.subscomponents.authorizeduser.subscomponents.main.subscomponents.home.HomeComponent
import tv.wfc.livestreamsales.ui.activity.main.fragments.base.PropertyOfMainActivity
import tv.wfc.livestreamsales.viewmodels.home.IHomeViewModel
import javax.inject.Inject

class HomeFragment: PropertyOfMainActivity(R.layout.fragment_home) {
    private var viewBinding: FragmentHomeBinding? = null

    lateinit var homeComponent: HomeComponent
        private set

    @Inject
    override lateinit var viewModel: IHomeViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        initializeHomeComponent()
        injectDependencies()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindView(view)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unbindView()
    }

    private fun initializeHomeComponent(){
        homeComponent = mainComponent.homeComponent().create(this)
    }

    private fun injectDependencies(){
        homeComponent.inject(this)
    }

    private fun bindView(view: View){
        viewBinding = FragmentHomeBinding.bind(view)
    }

    private fun unbindView(){
        viewBinding = null
    }
}