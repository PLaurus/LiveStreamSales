package tv.wfc.livestreamsales.features.mystreams.di.modules.viewmodel

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import tv.wfc.livestreamsales.application.di.modules.viewmodelprovider.mapkeys.ViewModelKey
import tv.wfc.livestreamsales.features.mystreams.di.qualifiers.MyStreamsFragment
import tv.wfc.livestreamsales.features.mystreams.di.scope.MyStreamsFeatureScope
import tv.wfc.livestreamsales.features.mystreams.viewmodel.IMyStreamsViewModel
import tv.wfc.livestreamsales.features.mystreams.viewmodel.MyStreamsViewModel

@Module
abstract class ViewModelModule {
    companion object {
        @Provides
        @MyStreamsFeatureScope
        internal fun provideIMyStreamsViewModel(
            @MyStreamsFragment
            fragment: Fragment,
            viewModelProviderFactory: ViewModelProvider.Factory
        ): IMyStreamsViewModel {
            return ViewModelProvider(
                fragment,
                viewModelProviderFactory
            )[MyStreamsViewModel::class.java]
        }
    }

    @Binds
    @IntoMap
    @ViewModelKey(MyStreamsViewModel::class)
    internal abstract fun bindMyStreamsViewModelIntoMap(
        viewModel: MyStreamsViewModel
    ): ViewModel
}