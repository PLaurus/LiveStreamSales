package com.example.livestreamsales.di.components.app.subscomponents.greeting

import androidx.appcompat.app.AppCompatActivity
import com.example.livestreamsales.di.components.app.subscomponents.greeting.modules.diffutils.DiffUtilsModule
import com.example.livestreamsales.di.components.app.subscomponents.greeting.modules.listadapters.ListAdaptersModule
import com.example.livestreamsales.di.components.app.subscomponents.greeting.modules.localstorage.GreetingLocalStorageModule
import com.example.livestreamsales.di.components.app.subscomponents.greeting.modules.repository.GreetingRepositoryModule
import com.example.livestreamsales.di.components.app.subscomponents.greeting.modules.viewmodel.GreetingViewModelModule
import com.example.livestreamsales.di.scopes.ActivityScope
import com.example.livestreamsales.ui.activity.greeting.GreetingActivity
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