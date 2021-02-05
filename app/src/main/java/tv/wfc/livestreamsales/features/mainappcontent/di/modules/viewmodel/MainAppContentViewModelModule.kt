package tv.wfc.livestreamsales.features.mainappcontent.di.modules.viewmodel

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import tv.wfc.livestreamsales.application.di.modules.viewmodelprovider.mapkeys.ViewModelKey
import tv.wfc.livestreamsales.features.mainappcontent.di.scope.MainAppContentFeatureScope
import tv.wfc.livestreamsales.features.mainappcontent.viewmodel.IMainAppContentViewModel
import tv.wfc.livestreamsales.features.mainappcontent.viewmodel.MainAppContentViewModel

@Module
abstract class MainAppContentViewModelModule {
    companion object{
        @MainAppContentFeatureScope
        @Provides
        @JvmStatic
        internal fun provideIMainViewModel(
            activity: AppCompatActivity,
            viewModelProviderFactory: ViewModelProvider.Factory
        ): IMainAppContentViewModel {
            return ViewModelProvider(
                activity,
                viewModelProviderFactory
            )[MainAppContentViewModel::class.java]
        }
    }

    @Binds
    @IntoMap
    @ViewModelKey(MainAppContentViewModel::class)
    abstract fun bindMainViewModelIntoMap(
        mainAppContentViewModel: MainAppContentViewModel
    ): ViewModel
}