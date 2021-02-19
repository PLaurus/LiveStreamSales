package tv.wfc.contentloader

import androidx.annotation.LayoutRes
import androidx.lifecycle.LifecycleOwner
import tv.wfc.contentloader.viewmodel.IToBePreparedViewModel

interface IContentLoaderView {
    fun attachViewModel(viewLifecycleOwner: LifecycleOwner, viewModel: IToBePreparedViewModel)
    fun attachContentView(@LayoutRes layoutResId: Int)
    fun showContent()
    fun showContentLoadingProgress()
    fun showContentLoadingError()
    fun showContentLoadingError(error: String)
    fun addOnDataIsNotPreparedListener(listener: () -> Unit): Boolean
    fun removeOnDataIsNotPreparedListener(listener: () -> Unit): Boolean
    fun clearOnDataIsNotPreparedListeners()
    fun addOnDataIsBeingPreparedListener(listener: () -> Unit): Boolean
    fun removeOnDataIsBeingPreparedListener(listener: () -> Unit): Boolean
    fun clearOnDataIsBeingPreparedListeners()
    fun addOnDataIsPreparedListener(listener: () -> Unit): Boolean
    fun removeOnDataIsPreparedListener(listener: () -> Unit): Boolean
    fun clearOnDataIsPreparedListeners()
    fun addOnDataPreparationFailureListener(listener: () -> Unit): Boolean
    fun removeOnDataPreparationFailureListener(listener: () -> Unit): Boolean
    fun clearOnDataPreparationFailureListeners()
    fun clearPreparationListeners()
    fun showOperationProgress()
    fun hideOperationProgress()
}