package com.example.livestreamsales.di.components.authorization

import androidx.appcompat.app.AppCompatActivity
import com.example.livestreamsales.di.components.authorization.modules.subcomponents.AuthorizationSubComponentsModule
import com.example.livestreamsales.di.components.authorization.modules.viewmodel.AuthorizationViewModelModule
import com.example.livestreamsales.di.components.phoneconfirmation.PhoneConfirmationComponent
import com.example.livestreamsales.di.components.phoneinput.PhoneInputComponent
import com.example.livestreamsales.di.scopes.ActivityScope
import com.example.livestreamsales.ui.activity.authorization.AuthorizationActivity
import dagger.BindsInstance
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = [
    AuthorizationViewModelModule::class,
    AuthorizationSubComponentsModule::class
])
interface AuthorizationComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance activity: AppCompatActivity): AuthorizationComponent
    }

    fun phoneInputComponent(): PhoneInputComponent.Factory
    fun phoneConfirmationComponent(): PhoneConfirmationComponent.Factory
    fun inject(activity: AuthorizationActivity)
}