package tv.wfc.livestreamsales.di.components.app.subscomponents.greeting

import androidx.appcompat.app.AppCompatActivity
import tv.wfc.livestreamsales.di.components.app.subscomponents.greeting.modules.diffutils.DiffUtilsModule
import tv.wfc.livestreamsales.di.components.app.subscomponents.greeting.modules.listadapters.ListAdaptersModule
import tv.wfc.livestreamsales.di.components.app.subscomponents.greeting.modules.localstorage.GreetingLocalStorageModule
import tv.wfc.livestreamsales.di.components.app.subscomponents.greeting.modules.repository.GreetingRepositoryModule
import tv.wfc.livestreamsales.di.components.app.subscomponents.greeting.modules.viewmodel.GreetingViewModelModule
import tv.wfc.livestreamsales.di.scopes.ActivityScope
import tv.wfc.livestreamsales.ui.activity.greeting.GreetingActivity
import dagger.BindsInstance
import dagger.Subcomponent

@ActivityScope
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
        fun create(@BindsInstance greetingActivity: AppCompatActivity): GreetingComponent
    }

    fun inject(activity: GreetingActivity)
}