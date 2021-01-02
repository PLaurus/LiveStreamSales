package com.example.livestreamsales.di.components.app

import com.example.livestreamsales.di.components.authorizeduser.AuthorizedUserComponent
import dagger.Module

@Module(subcomponents = [
    AuthorizedUserComponent::class
])
class SubComponentsModule