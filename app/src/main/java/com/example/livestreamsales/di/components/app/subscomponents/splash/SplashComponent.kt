package com.example.livestreamsales.di.components.app.subscomponents.splash

import androidx.appcompat.app.AppCompatActivity
import com.example.livestreamsales.di.components.app.subscomponents.splash.modules.subscomponents.SplashSubComponentsModule
import com.example.livestreamsales.di.components.app.subscomponents.splash.modules.viewmodel.SplashViewModelModule
import com.example.livestreamsales.di.scopes.ActivityScope
import com.example.livestreamsales.ui.activity.splash.SplashActivity
import dagger.BindsInstance
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = [
    SplashSubComponentsModule::class,
    SplashViewModelModule::class
])
interface SplashComponent {

    @Subcomponent.Factory
    interface Factory{
        fun create(@BindsInstance activity: AppCompatActivity): SplashComponent
    }

    fun inject(activity: SplashActivity)
}