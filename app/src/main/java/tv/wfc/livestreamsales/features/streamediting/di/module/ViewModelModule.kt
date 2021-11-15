package tv.wfc.livestreamsales.features.streamediting.di.module

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import tv.wfc.livestreamsales.application.di.modules.viewmodelprovider.mapkeys.ViewModelKey
import tv.wfc.livestreamsales.features.streamediting.di.qualifer.StreamEditingFragment
import tv.wfc.livestreamsales.features.streamediting.di.scope.StreamEditingFeatureScope
import tv.wfc.livestreamsales.features.streamediting.viewmodel.IStreamEditingViewModel
import tv.wfc.livestreamsales.features.streamediting.viewmodel.StreamEditingViewModel

@Module
abstract class ViewModelModule {
    companion object {
        @Provides
        @StreamEditingFeatureScope
        internal fun provideIStreamEditingViewModel(
            @StreamEditingFragment
            fragment: Fragment,
            viewModelFactory: ViewModelProvider.Factory
        ): IStreamEditingViewModel {
            return ViewModelProvider(
                fragment,
                viewModelFactory
            )[StreamEditingViewModel::class.java]
        }
    }

    @Binds
    @IntoMap
    @ViewModelKey(StreamEditingViewModel::class)
    internal abstract fun bindStreamEditingViewModelIntoMap(
        viewModel: StreamEditingViewModel
    ): ViewModel
}