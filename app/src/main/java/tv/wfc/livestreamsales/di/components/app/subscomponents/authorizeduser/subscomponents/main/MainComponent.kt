package tv.wfc.livestreamsales.di.components.app.subscomponents.authorizeduser.subscomponents.main

import androidx.appcompat.app.AppCompatActivity
import tv.wfc.livestreamsales.di.components.app.subscomponents.authorizeduser.subscomponents.main.modules.subcomponents.MainSubComponentsModule
import tv.wfc.livestreamsales.di.components.app.subscomponents.authorizeduser.subscomponents.main.modules.viewmodel.MainViewModelModule
import tv.wfc.livestreamsales.di.scopes.ActivityScope
import tv.wfc.livestreamsales.ui.activity.main.MainActivity
import dagger.BindsInstance
import dagger.Subcomponent
import tv.wfc.livestreamsales.di.components.app.subscomponents.authorizeduser.subscomponents.main.subscomponents.broadcastsinformation.BroadcastsInformationComponent
import tv.wfc.livestreamsales.di.components.app.subscomponents.authorizeduser.subscomponents.main.subscomponents.home.HomeComponent

@ActivityScope
@Subcomponent(modules = [
    MainSubComponentsModule::class,
    MainViewModelModule::class
])
interface MainComponent {
    @Subcomponent.Factory
    interface Factory{
        fun create(@BindsInstance activity: AppCompatActivity): MainComponent
    }

    fun inject(activity: MainActivity)

    fun homeComponent(): HomeComponent.Factory
    fun broadcastsInformationComponent(): BroadcastsInformationComponent.Factory
}