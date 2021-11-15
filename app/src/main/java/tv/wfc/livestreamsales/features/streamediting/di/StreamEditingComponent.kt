package tv.wfc.livestreamsales.features.streamediting.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import tv.wfc.livestreamsales.features.streamediting.di.module.ViewModelModule
import tv.wfc.livestreamsales.features.streamediting.di.qualifer.StreamEditingFragment
import tv.wfc.livestreamsales.features.streamediting.di.scope.StreamEditingFeatureScope

@StreamEditingFeatureScope
@Subcomponent(modules =[
    ViewModelModule::class
])
interface StreamEditingComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(
            @BindsInstance
            @StreamEditingFragment
            fragment: Fragment
        ) : StreamEditingComponent
    }

    fun inject(fragment: tv.wfc.livestreamsales.features.streamediting.ui.StreamEditingFragment)
}