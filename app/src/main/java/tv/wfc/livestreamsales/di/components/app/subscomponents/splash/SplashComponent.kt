package tv.wfc.livestreamsales.di.components.app.subscomponents.splash

import androidx.appcompat.app.AppCompatActivity
import tv.wfc.livestreamsales.di.components.app.subscomponents.splash.modules.subscomponents.SplashSubComponentsModule
import tv.wfc.livestreamsales.di.components.app.subscomponents.splash.modules.viewmodel.SplashViewModelModule
import tv.wfc.livestreamsales.di.scopes.ActivityScope
import tv.wfc.livestreamsales.ui.activity.splash.SplashActivity
import dagger.BindsInstance
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = [
    SplashSubComponentsModule::class,
    SplashViewModelModule::class
])
interface SplashComponent {

    @Subcomponent.Factory
    interface Factory{
        fun create(@BindsInstance activity: AppCompatActivity): SplashComponent
    }

    fun inject(activity: SplashActivity)
}