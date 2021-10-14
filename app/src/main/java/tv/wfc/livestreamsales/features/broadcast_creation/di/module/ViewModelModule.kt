package tv.wfc.livestreamsales.features.broadcast_creation.di.module

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import tv.wfc.livestreamsales.application.di.modules.viewmodelprovider.mapkeys.ViewModelKey
import tv.wfc.livestreamsales.features.broadcast_creation.di.qualifier.BroadcastCreationFragment
import tv.wfc.livestreamsales.features.broadcast_creation.di.scope.BroadcastCreationFeatureScope
import tv.wfc.livestreamsales.features.broadcast_creation.view_model.BroadcastCreationViewModel
import tv.wfc.livestreamsales.features.broadcast_creation.view_model.IBroadcastCreationViewModel

@Module
abstract class ViewModelModule {
    companion object {
        @Provides
        @BroadcastCreationFeatureScope
        internal fun provideIBroadcastCreationViewModel(
            @BroadcastCreationFragment
            fragment: Fragment,
            viewModelProviderFactory: ViewModelProvider.Factory

        ): IBroadcastCreationViewModel {
            return ViewModelProvider(
                fragment,
                viewModelProviderFactory
            )[BroadcastCreationViewModel::class.java]
        }
    }

    @Binds
    @IntoMap
    @ViewModelKey(BroadcastCreationViewModel::class)
    internal abstract fun bindBroadcastCreationViewModelIntoMap(
        viewModel: BroadcastCreationViewModel
    ): ViewModel
}