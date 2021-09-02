package tv.wfc.livestreamsales.features.mainappcontent.ui

import android.os.Bundle
import android.view.View
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import io.reactivex.rxjava3.disposables.CompositeDisposable
import tv.wfc.livestreamsales.R
import tv.wfc.livestreamsales.application.ui.base.BaseActivity
import tv.wfc.livestreamsales.databinding.ActivityMainAppContentBinding
import tv.wfc.livestreamsales.features.mainappcontent.di.MainAppContentComponent
import tv.wfc.livestreamsales.features.mainappcontent.viewmodel.IMainAppContentViewModel
import javax.inject.Inject

class MainAppContentActivity : BaseActivity() {
    private val disposables = CompositeDisposable()
    private val navigationController by lazy{
        findNavController(R.id.mainAppContentNavigationHostFragment)
    }
    private val toolbarNavigationOnClickListeners = mutableListOf<ToolbarNavigationOnClickListener>()

    private lateinit var viewBinding: ActivityMainAppContentBinding

    lateinit var mainAppContentComponent: MainAppContentComponent
        private set

    @Inject
    lateinit var mainAppContentViewModel: IMainAppContentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        initializeMainActivityComponent()
        injectDependencies()
        super.onCreate(savedInstanceState)
        bindView()
        initializeViews()
    }

    override fun onDestroy() {
        disposables.dispose()
        super.onDestroy()
    }

    fun hideToolbar(){
        viewBinding.toolbar.visibility = View.GONE
    }

    fun showToolbar(){
        viewBinding.toolbar.visibility = View.VISIBLE
    }

    fun setToolbarTitle(title: String){
        viewBinding.toolbar.title = title
    }

    fun clearToolbarTitle(){
        viewBinding.toolbar.title = ""
    }

    fun addToolbarNavigationOnClickListener(listener: ToolbarNavigationOnClickListener){
        toolbarNavigationOnClickListeners.add(listener)
    }

    fun removeToolbarNavigationOnClickListener(listener: ToolbarNavigationOnClickListener){
        toolbarNavigationOnClickListeners.remove(listener)
    }

    private fun initializeMainActivityComponent(){
        mainAppContentComponent = appComponent
            .mainAppContentComponent()
            .create(this)
    }

    private fun injectDependencies(){
        mainAppContentComponent.inject(this)
    }

    private fun bindView(){
        viewBinding = ActivityMainAppContentBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
    }

    private fun initializeViews(){
        initializeToolbar()
    }

    private fun initializeToolbar(){
        viewBinding.toolbar.apply {
            val appBarConfiguration = AppBarConfiguration(navigationController.graph)
            setSupportActionBar(this) // Must be called before toolbar.setupWithNavController!
            setupWithNavController(navigationController, appBarConfiguration)
            setNavigationOnClickListener{
                val actions = toolbarNavigationOnClickListeners.toList()
                actions.forEach { it.onClick() }
                NavigationUI.navigateUp(navigationController, appBarConfiguration)
            }
        }
    }

    fun interface ToolbarNavigationOnClickListener{
        fun onClick()
    }
}