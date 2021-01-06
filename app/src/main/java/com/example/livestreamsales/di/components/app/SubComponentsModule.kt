package com.example.livestreamsales.di.components.app

import com.example.livestreamsales.di.components.authorizeduser.AuthorizedUserComponent
import com.example.livestreamsales.di.components.mainactivity.MainActivityComponent
import dagger.Module

@Module(subcomponents = [
    MainActivityComponent::class,
    AuthorizedUserComponent::class
])
class SubComponentsModule