package com.example.livestreamsales.di.components.app.modules.subcomponents

import com.example.livestreamsales.di.components.app.subscomponents.authorization.AuthorizationComponent
import com.example.livestreamsales.di.components.app.subscomponents.authorizeduser.AuthorizedUserComponent
import com.example.livestreamsales.di.components.app.subscomponents.greeting.GreetingComponent
import com.example.livestreamsales.di.components.app.subscomponents.splash.SplashComponent
import dagger.Module

@Module(subcomponents = [
    SplashComponent::class,
    AuthorizationComponent::class,
    GreetingComponent::class,
    AuthorizedUserComponent::class
])
class AppSubComponentsModule