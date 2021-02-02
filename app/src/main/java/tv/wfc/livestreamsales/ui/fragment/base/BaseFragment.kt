package tv.wfc.livestreamsales.ui.fragment.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import tv.wfc.livestreamsales.R
import tv.wfc.livestreamsales.databinding.FragmentBaseBinding
import tv.wfc.livestreamsales.model.application.viewmodel.ViewModelPreparationState
import tv.wfc.livestreamsales.viewmodels.base.IToBePreparedViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable

abstract class BaseFragment(
    @LayoutRes
    private val contentLayoutId: Int
): Fragment() {
    protected lateinit var viewScopeDisposables: CompositeDisposable
    private var viewBinding: FragmentBaseBinding? = null

    abstract val viewModel: IToBePreparedViewModel

    @CallSuper
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewScopeDisposables = CompositeDisposable()

        val view = bindView(inflater, container)
        inflateContentView(view.context)

        return view
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        manageContentAndProgressVisibility()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewScopeDisposables.dispose()
        unbindView()
    }

    protected open fun onDataIsNotPrepared() = Unit

    @CallSuper
    protected open fun onDataIsBeingPrepared(){
        showContentLoadingProgress()
    }

    @CallSuper
    protected open fun onDataIsPrepared() {
        showContent()
    }

    @CallSuper
    protected open fun onDataPreparationFailure(){
        showContentLoadingError()
    }

    protected fun showOperationProgress(){
        viewBinding?.operationProgressContainer?.visibility = View.VISIBLE
    }

    protected fun hideOperationProgress(){
        viewBinding?.operationProgressContainer?.visibility = View.INVISIBLE
    }

    private fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): View{
        val viewBinding = FragmentBaseBinding.inflate(inflater, parent, false)
        this.viewBinding = viewBinding
        return viewBinding.root.apply{
            replaceEmptyBackground()
        }
    }

    private fun unbindView(){
        viewBinding = null
    }

    private fun inflateContentView(context: Context): View?{
        var contentView: View? = null

        viewBinding?.contentContainer?.let{
            contentView = LayoutInflater.from(context).inflate(contentLayoutId, it, false).apply{
                layoutParams.apply {
                    width = ViewGroup.LayoutParams.MATCH_PARENT
                    height = ViewGroup.LayoutParams.MATCH_PARENT
                }
            }

            it.addView(contentView)
            it.requestLayout()
        }

        return contentView
    }

    private fun manageContentAndProgressVisibility(){
        viewModel.dataPreparationState.observe(viewLifecycleOwner, { dataPreparationState ->
            when(dataPreparationState){
                is ViewModelPreparationState.DataIsBeingPrepared -> onDataIsBeingPrepared()
                is ViewModelPreparationState.DataIsPrepared -> onDataIsPrepared()
                is ViewModelPreparationState.FailedToPrepareData -> onDataPreparationFailure()
                else -> onDataIsNotPrepared()
            }
        })
    }

    private fun showContent(){
        viewBinding?.apply{
            contentContainer.visibility = View.VISIBLE
            contentProgressContainer.visibility = View.INVISIBLE
            contentIsNotLoadedMessage.visibility = View.INVISIBLE
        }
    }

    private fun showContentLoadingProgress(){
        viewBinding?.apply{
            contentContainer.visibility = View.INVISIBLE
            contentIsNotLoadedMessage.visibility = View.INVISIBLE
            contentProgressContainer.visibility = View.VISIBLE
        }
    }

    private fun showContentLoadingError(
        error: String = resources.getString(R.string.base_fragment_content_loading_error)
    ){
        viewBinding?.apply {
            contentContainer.visibility = View.INVISIBLE
            contentProgressContainer.visibility = View.INVISIBLE
            contentIsNotLoadedMessage.apply{
                text = error
                visibility = View.VISIBLE
            }
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