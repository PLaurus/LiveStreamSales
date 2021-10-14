package tv.wfc.livestreamsales.features.broadcast_editing.di.module

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import tv.wfc.livestreamsales.application.di.modules.viewmodelprovider.mapkeys.ViewModelKey
import tv.wfc.livestreamsales.features.broadcast_editing.di.qualifer.BroadcastEditingFragment
import tv.wfc.livestreamsales.features.broadcast_editing.di.scope.BroadcastEditingFeatureScope
import tv.wfc.livestreamsales.features.broadcast_editing.view_model.BroadcastEditingViewModel
import tv.wfc.livestreamsales.features.broadcast_editing.view_model.IBroadcastEditingViewModel

@Module
abstract class ViewModelModule {
    companion object {
        @Provides
        @BroadcastEditingFeatureScope
        internal fun provideIBroadcastEditingViewModel(
            @BroadcastEditingFragment
            fragment: Fragment,
            viewModelFactory: ViewModelProvider.Factory
        ): IBroadcastEditingViewModel {
            return ViewModelProvider(
                fragment,
                viewModelFactory
            )[BroadcastEditingViewModel::class.java]
        }
    }

    @Binds
    @IntoMap
    @ViewModelKey(BroadcastEditingViewModel::class)
    internal abstract fun bindBroadcastEditingViewModelIntoMap(
        viewModel: BroadcastEditingViewModel
    ): ViewModel
}