package tv.wfc.livestreamsales.application.ui.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import io.reactivex.rxjava3.disposables.CompositeDisposable
import tv.wfc.livestreamsales.R
import tv.wfc.livestreamsales.application.LiveStreamSalesApplication
import tv.wfc.livestreamsales.application.di.AppComponent

abstract class BaseFragment(
    @LayoutRes
    private val contentLayoutId: Int
): Fragment(contentLayoutId) {
    protected lateinit var viewScopeDisposables: CompositeDisposable
        private set

    protected lateinit var appComponent: AppComponent
        private set

    @CallSuper
    override fun onAttach(context: Context) {
        super.onAttach(context)
        initializeAppComponent(context)
    }

    final override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewScopeDisposables = CompositeDisposable()

        return super.onCreateView(inflater, container, savedInstanceState)?.apply{
            replaceEmptyBackground()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewScopeDisposables.dispose()
    }

    private fun initializeAppComponent(context: Context){
        appComponent = (context.applicationContext as LiveStreamSalesApplication).appComponent
    }

    private fun View.replaceEmptyBackground(){
        if(background == null){
            setDefaultBackground()
        }
    }

    private fun View.setDefaultBackground(){
        setBackgroundColor(ContextCompat.getColor(context, R.color.defaultFragmentBackground))
    }
}