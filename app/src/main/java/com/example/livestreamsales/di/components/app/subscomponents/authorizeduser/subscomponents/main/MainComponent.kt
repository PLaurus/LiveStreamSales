package com.example.livestreamsales.di.components.app.subscomponents.authorizeduser.subscomponents.main

import androidx.appcompat.app.AppCompatActivity
import com.example.livestreamsales.di.components.app.subscomponents.authorizeduser.subscomponents.main.modules.subcomponents.MainSubComponentsModule
import com.example.livestreamsales.di.components.app.subscomponents.authorizeduser.subscomponents.main.modules.viewmodel.MainViewModelModule
import com.example.livestreamsales.di.scopes.ActivityScope
import com.example.livestreamsales.ui.activity.main.MainActivity
import dagger.BindsInstance
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = [
    MainSubComponentsModule::class,
    MainViewModelModule::class
])
interface MainComponent {

    @Subcomponent.Factory
    interface Factory{
        fun create(@BindsInstance activity: AppCompatActivity): MainComponent
    }

    fun inject(activity: MainActivity)
}