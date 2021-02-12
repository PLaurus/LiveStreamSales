package tv.wfc.livestreamsales.features.home.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import tv.wfc.livestreamsales.R
import tv.wfc.livestreamsales.databinding.FragmentHomeBinding
import tv.wfc.livestreamsales.features.authorizeduser.ui.base.AuthorizedUserFragment
import tv.wfc.livestreamsales.features.home.di.HomeComponent
import tv.wfc.livestreamsales.features.home.viewmodel.IHomeViewModel
import javax.inject.Inject

class HomeFragment: AuthorizedUserFragment(R.layout.fragment_home) {
    private val homeNavigationController by lazy {
        requireActivity().findNavController(R.id.homeDestinationNavigationFragment)
    }

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

    override fun onDataIsPrepared() {
        super.onDataIsPrepared()
        initializeBottomNavigation()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unbindView()
    }

    private fun initializeHomeComponent(){
        homeComponent = authorizedUserComponent.homeComponent().create(this)
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

    private fun initializeBottomNavigation(){
        viewBinding?.bottomNavigation?.setupWithNavController(homeNavigationController)
    }
}