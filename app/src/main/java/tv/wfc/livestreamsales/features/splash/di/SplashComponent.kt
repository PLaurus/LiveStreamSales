package tv.wfc.livestreamsales.features.splash.di

import androidx.appcompat.app.AppCompatActivity
import dagger.BindsInstance
import dagger.Subcomponent
import tv.wfc.livestreamsales.features.splash.di.modules.viewmodel.SplashViewModelModule
import tv.wfc.livestreamsales.features.splash.di.scope.SplashFeatureScope
import tv.wfc.livestreamsales.features.splash.ui.SplashActivity

@SplashFeatureScope
@Subcomponent(modules = [
    SplashViewModelModule::class
])
interface SplashComponent {
    @Subcomponent.Factory
    interface Factory{
        fun create(
            @BindsInstance activity: AppCompatActivity
        ): SplashComponent
    }

    fun inject(activity: SplashActivity)
}