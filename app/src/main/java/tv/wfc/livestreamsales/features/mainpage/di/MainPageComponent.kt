package tv.wfc.livestreamsales.features.mainpage.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import tv.wfc.livestreamsales.features.mainpage.di.modules.api.ApiModule
import tv.wfc.livestreamsales.features.mainpage.di.modules.diffutils.DiffUtilsModule
import tv.wfc.livestreamsales.features.mainpage.di.modules.repository.RepositoryModule
import tv.wfc.livestreamsales.features.mainpage.di.modules.storage.StorageModule
import tv.wfc.livestreamsales.features.mainpage.di.modules.viewmodel.MainPageViewModelModule
import tv.wfc.livestreamsales.features.mainpage.di.qualifiers.MainPageFragment
import tv.wfc.livestreamsales.features.mainpage.di.scope.MainPageFeatureScope

@MainPageFeatureScope
@Subcomponent(modules = [
    MainPageViewModelModule::class,
    ApiModule::class,
    StorageModule::class,
    RepositoryModule::class,
    DiffUtilsModule::class
])
interface MainPageComponent {
    @Subcomponent.Factory
    interface Factory{
        fun create(
            @BindsInstance
            @MainPageFragment
            fragment: Fragment
        ): MainPageComponent
    }

    fun inject(fragment: tv.wfc.livestreamsales.features.mainpage.ui.MainPageFragment)
}