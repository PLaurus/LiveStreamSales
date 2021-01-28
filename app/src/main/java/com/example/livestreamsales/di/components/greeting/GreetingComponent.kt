package com.example.livestreamsales.di.components.greeting

import androidx.appcompat.app.AppCompatActivity
import com.example.livestreamsales.di.components.greeting.modules.subcomponents.GreetingSubComponentsModule
import com.example.livestreamsales.di.components.greeting.modules.viewmodel.GreetingViewModelModule
import com.example.livestreamsales.di.scopes.ActivityScope
import com.example.livestreamsales.ui.activity.greeting.GreetingActivity
import dagger.BindsInstance
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = [
    GreetingSubComponentsModule::class,
    GreetingViewModelModule::class
])
interface GreetingComponent {

    @Subcomponent.Factory
    interface Factory{
        fun create(@BindsInstance greetingActivity: AppCompatActivity): GreetingComponent
    }

    fun inject(activity: GreetingActivity)
}