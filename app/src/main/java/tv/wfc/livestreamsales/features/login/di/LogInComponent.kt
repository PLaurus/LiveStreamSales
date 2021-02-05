package tv.wfc.livestreamsales.features.login.di

import androidx.appcompat.app.AppCompatActivity
import dagger.BindsInstance
import dagger.Subcomponent
import tv.wfc.livestreamsales.features.login.di.modules.repository.LogInRepositoryModule
import tv.wfc.livestreamsales.features.login.di.modules.storage.LogInStorageModule
import tv.wfc.livestreamsales.features.login.di.modules.viewmodel.LogInViewModelModule
import tv.wfc.livestreamsales.features.login.di.scope.LogInFeatureScope
import tv.wfc.livestreamsales.features.login.ui.LogInActivity
import tv.wfc.livestreamsales.features.phonenumberconfirmation.di.PhoneNumberConfirmationComponent
import tv.wfc.livestreamsales.features.phonenumberinput.di.PhoneNumberInputComponent

@LogInFeatureScope
@Subcomponent(modules = [
    LogInViewModelModule::class,
    LogInRepositoryModule::class,
    LogInStorageModule::class
])
interface LogInComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(
            @BindsInstance
            activity: AppCompatActivity
        ): LogInComponent
    }

    fun phoneNumberInputComponent(): PhoneNumberInputComponent.Factory
    fun phoneNumberConfirmationComponent(): PhoneNumberConfirmationComponent.Factory

    fun inject(activity: LogInActivity)
}