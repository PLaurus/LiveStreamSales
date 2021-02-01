package tv.wfc.livestreamsales.di.components.app.subscomponents.login

import androidx.appcompat.app.AppCompatActivity
import tv.wfc.livestreamsales.di.components.app.subscomponents.login.modules.subcomponents.LogInSubComponentsModule
import tv.wfc.livestreamsales.di.components.app.subscomponents.login.modules.viewmodel.LogInViewModelModule
import tv.wfc.livestreamsales.di.components.app.subscomponents.login.subcomponents.phonenumberconfirmation.PhoneNumberConfirmationComponent
import tv.wfc.livestreamsales.di.components.app.subscomponents.login.subcomponents.phonenumberinput.PhoneNumberInputComponent
import tv.wfc.livestreamsales.di.scopes.ActivityScope
import tv.wfc.livestreamsales.ui.activity.login.LogInActivity
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