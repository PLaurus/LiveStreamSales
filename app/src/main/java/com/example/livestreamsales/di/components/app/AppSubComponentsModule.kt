package com.example.livestreamsales.di.components.app

import com.example.livestreamsales.di.components.authorization.AuthorizationComponent
import com.example.livestreamsales.di.components.authorizeduser.AuthorizedUserComponent
import com.example.livestreamsales.di.components.main.MainComponent
import com.example.livestreamsales.di.components.splash.SplashComponent
import dagger.Module

@Module(subcomponents = [
    SplashComponent::class,
    AuthorizationComponent::class,
    MainComponent::class,
    AuthorizedUserComponent::class
])
class AppSubComponentsModule