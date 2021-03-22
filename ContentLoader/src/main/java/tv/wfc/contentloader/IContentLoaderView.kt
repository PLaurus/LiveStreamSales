package tv.wfc.contentloader

import android.view.View
import androidx.annotation.LayoutRes
import androidx.lifecycle.LifecycleOwner
import tv.wfc.contentloader.viewmodel.INeedPreparationViewModel

interface IContentLoaderView {
    fun attachViewModel(viewLifecycleOwner: LifecycleOwner, viewModel: INeedPreparationViewModel)
    fun attachContentView(@LayoutRes layoutResId: Int)
    fun attachContentView(view: View)
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