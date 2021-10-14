package tv.wfc.livestreamsales.features.broadcast_creation.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import tv.wfc.livestreamsales.features.broadcast_creation.di.module.ViewModelModule
import tv.wfc.livestreamsales.features.broadcast_creation.di.qualifier.BroadcastCreationFragment
import tv.wfc.livestreamsales.features.broadcast_creation.di.scope.BroadcastCreationFeatureScope

@BroadcastCreationFeatureScope
@Subcomponent(modules = [
    ViewModelModule::class
])
interface BroadcastCreationComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(
            @BindsInstance
            @BroadcastCreationFragment
            fragment: Fragment
        ): BroadcastCreationComponent
    }

    fun inject(fragment: tv.wfc.livestreamsales.features.broadcast_creation.ui.BroadcastCreationFragment)
}