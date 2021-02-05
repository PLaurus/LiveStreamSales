package tv.wfc.livestreamsales.features.mainappcontent.di

import androidx.appcompat.app.AppCompatActivity
import dagger.BindsInstance
import dagger.Subcomponent
import tv.wfc.livestreamsales.features.mainappcontent.di.modules.viewmodel.MainAppContentViewModelModule
import tv.wfc.livestreamsales.features.mainappcontent.di.scope.MainAppContentFeatureScope
import tv.wfc.livestreamsales.features.mainappcontent.ui.MainAppContentActivity

@MainAppContentFeatureScope
@Subcomponent(modules = [
    MainAppContentViewModelModule::class
])
interface MainAppContentComponent {
    @Subcomponent.Factory
    interface Factory{
        fun create(
            @BindsInstance
            activity: AppCompatActivity
        ): MainAppContentComponent
    }

    fun inject(activity: MainAppContentActivity)
}