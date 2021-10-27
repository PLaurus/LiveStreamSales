package tv.wfc.livestreamsales.features.streamcreation.di.module

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import tv.wfc.livestreamsales.application.di.modules.viewmodelprovider.mapkeys.ViewModelKey
import tv.wfc.livestreamsales.features.streamcreation.di.qualifier.StreamCreationFragment
import tv.wfc.livestreamsales.features.streamcreation.di.scope.StreamCreationFeatureScope
import tv.wfc.livestreamsales.features.streamcreation.viewmodel.IStreamCreationViewModel
import tv.wfc.livestreamsales.features.streamcreation.viewmodel.StreamCreationViewModel

@Module
abstract class ViewModelModule {
    companion object {
        @Provides
        @StreamCreationFeatureScope
        internal fun provideIStreamCreationViewModel(
            @StreamCreationFragment
            fragment: Fragment,
            viewModelProviderFactory: ViewModelProvider.Factory

        ): IStreamCreationViewModel {
            return ViewModelProvider(
                fragment,
                viewModelProviderFactory
            )[StreamCreationViewModel::class.java]
        }
    }

    @Binds
    @IntoMap
    @ViewModelKey(StreamCreationViewModel::class)
    internal abstract fun bindStreamCreationViewModelIntoMap(
        viewModel: StreamCreationViewModel
    ): ViewModel
}