package com.example.livestreamsales.di.components.authorizeduser

import com.example.livestreamsales.di.scopes.LoggedInUserScope
import dagger.Subcomponent

@LoggedInUserScope
@Subcomponent(modules = [

])
interface AuthorizedUserComponent {

    @Subcomponent.Factory
    interface Factory{
        fun create(): AuthorizedUserComponent
    }
}