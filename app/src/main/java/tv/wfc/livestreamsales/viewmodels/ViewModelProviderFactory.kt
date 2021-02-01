package tv.wfc.livestreamsales.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider

class ViewModelProviderFactory @Inject constructor(
    private val viewModelDependencyProviders: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
): ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModelDependencyProvider: Provider<ViewModel> = viewModelDependencyProviders[modelClass] ?:
            throw IllegalArgumentException("Model class $modelClass not found")

        return viewModelDependencyProvider.get() as T
    }
}