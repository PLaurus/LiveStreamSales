package tv.wfc.livestreamsales.features.streamcreation.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import tv.wfc.livestreamsales.features.streamcreation.di.module.ViewModelModule
import tv.wfc.livestreamsales.features.streamcreation.di.qualifier.StreamCreationFragment
import tv.wfc.livestreamsales.features.streamcreation.di.scope.StreamCreationFeatureScope

@StreamCreationFeatureScope
@Subcomponent(modules = [
    ViewModelModule::class
])
interface StreamCreationComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(
            @BindsInstance
            @StreamCreationFragment
            fragment: Fragment
        ): StreamCreationComponent
    }

    fun inject(fragment: tv.wfc.livestreamsales.features.streamcreation.ui.StreamCreationFragment)
}