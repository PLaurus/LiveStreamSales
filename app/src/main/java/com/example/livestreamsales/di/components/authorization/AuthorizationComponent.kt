package com.example.livestreamsales.di.components.authorization

import androidx.appcompat.app.AppCompatActivity
import com.example.livestreamsales.di.components.telephonenumberinput.TelephoneNumberInputComponent
import com.example.livestreamsales.di.scopes.ActivityScope
import com.example.livestreamsales.ui.activity.authorization.AuthorizationActivity
import dagger.BindsInstance
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = [
    ViewModelModule::class,
    SubComponentsModule::class
])
interface AuthorizationComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance activity: AppCompatActivity): AuthorizationComponent
    }

    fun telephoneNumberInputComponent(): TelephoneNumberInputComponent.Factory
    fun inject(activity: AuthorizationActivity)
}