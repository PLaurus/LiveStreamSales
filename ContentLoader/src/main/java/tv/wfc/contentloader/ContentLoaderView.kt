package tv.wfc.contentloader

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import tv.wfc.contentloader.databinding.LayoutContentLoaderBinding
import tv.wfc.contentloader.model.ViewModelPreparationState
import tv.wfc.contentloader.viewmodel.IToBePreparedViewModel

class ContentLoaderView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): ConstraintLayout(context, attrs, defStyleAttr), IContentLoaderView{
    private val layoutInflater = LayoutInflater.from(context)
    private val viewBinding = LayoutContentLoaderBinding.inflate(layoutInflater, this)

    private val viewModelPreparationStateObserver = Observer<ViewModelPreparationState> { dataPreparationState ->
        when(dataPreparationState){
            is ViewModelPreparationState.DataIsBeingPrepared -> onDataIsBeingPrepared()
            is ViewModelPreparationState.DataIsPrepared -> onDataIsPrepared()
            is ViewModelPreparationState.FailedToPrepareData -> onDataPreparationFailure()
            else -> onDataIsNotPrepared()
        }
    }

    private val onDataIsNotPreparedListeners = mutableSetOf<() -> Unit>()
    private val onDataIsBeingPreparedListeners = mutableSetOf<() -> Unit>()
    private val onDataIsPreparedListeners = mutableSetOf<() -> Unit>()
    private val onDataPreparationFailureListeners = mutableSetOf<() -> Unit>()

    private var toBePreparedViewModel: IToBePreparedViewModel? = null

    var contentView: View = viewBinding.contentContainer
        private set

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMeasureMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMeasureMode = MeasureSpec.getMode(heightMeasureSpec)

        val contentWidth = if(widthMeasureMode == MeasureSpec.AT_MOST){
            ViewGroup.LayoutParams.WRAP_CONTENT
        } else{
            ViewGroup.LayoutParams.MATCH_PARENT
        }

        val contentHeight = if(heightMeasureMode == MeasureSpec.AT_MOST){
            ViewGroup.LayoutParams.WRAP_CONTENT
        } else{
            ViewGroup.LayoutParams.MATCH_PARENT
        }

        viewBinding.contentContainer.updateLayoutParams {
            width = contentWidth
            height = contentHeight
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun attachViewModel(
        viewLifecycleOwner: LifecycleOwner,
        viewModel: IToBePreparedViewModel
    ) {
        toBePreparedViewModel?.dataPreparationState?.removeObserver(viewModelPreparationStateObserver)
        viewModel.dataPreparationState.observe(viewLifecycleOwner, viewModelPreparationStateObserver)
        toBePreparedViewModel = viewModel
    }

    override fun attachContentView(
        @LayoutRes
        layoutResId: Int
    ) {
        clearContentView()

        viewBinding.contentContainer.apply{
            val contentView = layoutInflater.inflate(layoutResId, this, false).apply{
                layoutParams.apply {
                    width = ViewGroup.LayoutParams.MATCH_PARENT
                    height = ViewGroup.LayoutParams.MATCH_PARENT
                }
            }

            addView(contentView)
            this@ContentLoaderView.contentView = contentView
            requestLayout()
        }
    }

    override fun showContent(){
        viewBinding.apply{
            contentContainer.visibility = View.VISIBLE
            contentProgressContainer.visibility = View.INVISIBLE
            contentIsNotLoadedMessage.visibility = View.INVISIBLE
        }
    }

    override fun showContentLoadingProgress(){
        viewBinding.apply{
            contentContainer.visibility = View.INVISIBLE
            contentIsNotLoadedMessage.visibility = View.INVISIBLE
            contentProgressContainer.visibility = View.VISIBLE
        }
    }

    override fun showContentLoadingError() {
        showContentLoadingError(resources.getString(R.string.content_loading_error))
    }

    override fun showContentLoadingError(error: String){
        viewBinding.apply {
            contentContainer.visibility = View.INVISIBLE
            contentProgressContainer.visibility = View.INVISIBLE
            contentIsNotLoadedMessage.apply{
                text = error
                visibility = View.VISIBLE
            }
        }
    }

    override fun addOnDataIsNotPreparedListener(listener: () -> Unit): Boolean{
        return onDataIsNotPreparedListeners.add(listener)
    }

    override fun removeOnDataIsNotPreparedListener(listener: () -> Unit): Boolean{
        return onDataIsNotPreparedListeners.remove(listener)
    }

    override fun clearOnDataIsNotPreparedListeners() {
        onDataIsNotPreparedListeners.clear()
    }

    override fun addOnDataIsBeingPreparedListener(listener: () -> Unit): Boolean{
        return onDataIsBeingPreparedListeners.add(listener)
    }

    override fun removeOnDataIsBeingPreparedListener(listener: () -> Unit): Boolean{
        return onDataIsBeingPreparedListeners.remove(listener)
    }

    override fun clearOnDataIsBeingPreparedListeners() {
        onDataIsBeingPreparedListeners.clear()
    }

    override fun addOnDataIsPreparedListener(listener: () -> Unit): Boolean{
        return onDataIsPreparedListeners.add(listener)
    }

    override fun removeOnDataIsPreparedListener(listener: () -> Unit): Boolean{
        return onDataIsPreparedListeners.remove(listener)
    }

    override fun clearOnDataIsPreparedListeners() {
        onDataIsPreparedListeners.clear()
    }

    override fun addOnDataPreparationFailureListener(listener: () -> Unit): Boolean{
        return onDataPreparationFailureListeners.add(listener)
    }

    override fun removeOnDataPreparationFailureListener(listener: () -> Unit): Boolean{
        return onDataPreparationFailureListeners.remove(listener)
    }

    override fun clearOnDataPreparationFailureListeners() {
        onDataPreparationFailureListeners.clear()
    }

    override fun clearPreparationListeners() {
        clearOnDataIsNotPreparedListeners()
        clearOnDataIsBeingPreparedListeners()
        clearOnDataIsPreparedListeners()
        clearOnDataPreparationFailureListeners()
    }

    override fun showOperationProgress() {
        viewBinding.operationProgressContainer.visibility = View.VISIBLE
    }

    override fun hideOperationProgress() {
        viewBinding.operationProgressContainer.visibility = View.INVISIBLE
    }

    private fun onDataIsNotPrepared(){
        onDataIsNotPreparedListeners.forEach { it() }
    }

    private fun onDataIsBeingPrepared(){
        showContentLoadingProgress()
        onDataIsBeingPreparedListeners.forEach { it() }
    }

    private fun onDataIsPrepared() {
        showContent()
        onDataIsPreparedListeners.forEach { it() }
    }

    private fun onDataPreparationFailure(){
        showContentLoadingError()
        onDataPreparationFailureListeners.forEach { it() }
    }

    private fun clearContentView(){
        viewBinding.contentContainer.removeAllViews()
        contentView = viewBinding.contentContainer
    }
}