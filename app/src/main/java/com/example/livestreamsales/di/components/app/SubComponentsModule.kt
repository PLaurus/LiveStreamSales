package com.example.livestreamsales.di.components.app

import com.example.livestreamsales.di.components.authorizeduser.AuthorizedUserComponent
import com.example.livestreamsales.di.components.mainactivity.MainActivityComponent
import com.example.livestreamsales.di.components.splash.SplashComponent
import dagger.Module

@Module(subcomponents = [
    SplashComponent::class,
    MainActivityComponent::class,
    AuthorizedUserComponent::class
])
class SubComponentsModule