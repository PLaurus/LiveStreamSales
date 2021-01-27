package com.example.livestreamsales.di.components.splash

import androidx.appcompat.app.AppCompatActivity
import com.example.livestreamsales.di.components.splash.modules.greeting.GreetingModule
import com.example.livestreamsales.di.components.splash.modules.subscomponents.GreetingSubComponentsModule
import com.example.livestreamsales.di.scopes.ActivityScope
import com.example.livestreamsales.ui.activity.splash.SplashScreenActivity
import dagger.BindsInstance
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = [
    GreetingSubComponentsModule::class,
    GreetingModule::class
])
interface SplashComponent {

    @Subcomponent.Factory
    interface Factory{
        fun create(@BindsInstance activity: AppCompatActivity): SplashComponent
    }

    fun inject(activity: SplashScreenActivity)
}