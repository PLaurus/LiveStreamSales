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
import tv.wfc.contentloader.ContentLoaderView
import tv.wfc.contentloader.viewmodel.IToBePreparedViewModel
import tv.wfc.livestreamsales.R
import tv.wfc.livestreamsales.application.LiveStreamSalesApplication
import tv.wfc.livestreamsales.application.di.AppComponent

abstract class BaseFragment(
    @LayoutRes
    private val contentLayoutId: Int
): Fragment() {
    private lateinit var contentLoaderView: ContentLoaderView

    protected lateinit var viewScopeDisposables: CompositeDisposable
        private set

    protected lateinit var appComponent: AppComponent
        private set

    abstract val viewModel: IToBePreparedViewModel

    @CallSuper
    override fun onAttach(context: Context) {
        super.onAttach(context)
        appComponent = (context.applicationContext as LiveStreamSalesApplication).appComponent
    }

    final override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewScopeDisposables = CompositeDisposable()

        contentLoaderView = createContentLoaderView(requireContext(), contentLayoutId)

        return contentLoaderView
    }

    final override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onContentViewCreated(contentLoaderView.contentView, savedInstanceState)
    }

    @CallSuper
    protected open fun onContentViewCreated(view: View, savedInstanceState: Bundle?) = Unit

    override fun onDestroyView() {
        super.onDestroyView()
        viewScopeDisposables.dispose()
        contentLoaderView.clearPreparationListeners()
    }

    protected open fun onDataIsNotPrepared() = Unit

    protected open fun onDataIsBeingPrepared() = Unit

    protected open fun onDataIsPrepared() = Unit

    protected open fun onDataPreparationFailure() = Unit

    protected fun showOperationProgress(){
        contentLoaderView.showOperationProgress()
    }

    protected fun hideOperationProgress(){
        contentLoaderView.hideOperationProgress()
    }

    private fun createContentLoaderView(
        context: Context,
        @LayoutRes
        contentLayoutId: Int
    ): ContentLoaderView{
        return ContentLoaderView(context).apply {
            attachContentView(contentLayoutId)
            attachViewModel(viewLifecycleOwner, viewModel)
            addOnDataIsNotPreparedListener(::onDataIsNotPrepared)
            addOnDataIsBeingPreparedListener(::onDataIsBeingPrepared)
            addOnDataIsPreparedListener(::onDataIsPrepared)
            addOnDataPreparationFailureListener(::onDataPreparationFailure)
            replaceEmptyBackground()
        }
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