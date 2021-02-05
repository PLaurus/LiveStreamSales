package tv.wfc.livestreamsales.features.greeting.di

import androidx.appcompat.app.AppCompatActivity
import dagger.BindsInstance
import dagger.Subcomponent
import tv.wfc.livestreamsales.features.greeting.di.modules.diffutils.DiffUtilsModule
import tv.wfc.livestreamsales.features.greeting.di.modules.listadapters.ListAdaptersModule
import tv.wfc.livestreamsales.features.greeting.di.modules.localstorage.GreetingLocalStorageModule
import tv.wfc.livestreamsales.features.greeting.di.modules.repository.GreetingRepositoryModule
import tv.wfc.livestreamsales.features.greeting.di.modules.viewmodel.GreetingViewModelModule
import tv.wfc.livestreamsales.features.greeting.di.scope.GreetingFeatureScope
import tv.wfc.livestreamsales.features.greeting.ui.GreetingActivity

@GreetingFeatureScope
@Subcomponent(modules = [
    GreetingViewModelModule::class,
    GreetingLocalStorageModule::class,
    GreetingRepositoryModule::class,
    ListAdaptersModule::class,
    DiffUtilsModule::class
])
interface GreetingComponent {
    @Subcomponent.Factory
    interface Factory{
        fun create(
            @BindsInstance
            greetingActivity: AppCompatActivity
        ): GreetingComponent
    }

    fun inject(activity: GreetingActivity)
}