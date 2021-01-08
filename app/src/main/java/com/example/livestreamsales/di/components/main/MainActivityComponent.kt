package com.example.livestreamsales.di.components.main

import androidx.appcompat.app.AppCompatActivity
import com.example.livestreamsales.di.scopes.ActivityScope
import com.example.livestreamsales.ui.activity.main.MainActivity
import dagger.BindsInstance
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = [
    ViewModelModule::class
])
interface MainActivityComponent {

    @Subcomponent.Factory
    interface Factory{
        fun create(@BindsInstance activity: AppCompatActivity): MainActivityComponent
    }

    fun inject(activity: MainActivity)
}