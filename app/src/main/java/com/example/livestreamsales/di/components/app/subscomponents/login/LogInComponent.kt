package com.example.livestreamsales.di.components.app.subscomponents.login

import androidx.appcompat.app.AppCompatActivity
import com.example.livestreamsales.di.components.app.subscomponents.login.modules.subcomponents.LogInSubComponentsModule
import com.example.livestreamsales.di.components.app.subscomponents.login.modules.viewmodel.LogInViewModelModule
import com.example.livestreamsales.di.components.app.subscomponents.login.subcomponents.phonenumberconfirmation.PhoneNumberConfirmationComponent
import com.example.livestreamsales.di.components.app.subscomponents.login.subcomponents.phonenumberinput.PhoneNumberInputComponent
import com.example.livestreamsales.di.scopes.ActivityScope
import com.example.livestreamsales.ui.activity.login.LogInActivity
import dagger.BindsInstance
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = [
    LogInViewModelModule::class,
    LogInSubComponentsModule::class
])
interface LogInComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance activity: AppCompatActivity): LogInComponent
    }

    fun phoneNumberInputComponent(): PhoneNumberInputComponent.Factory
    fun phoneNumberConfirmationComponent(): PhoneNumberConfirmationComponent.Factory
    fun inject(activity: LogInActivity)
}