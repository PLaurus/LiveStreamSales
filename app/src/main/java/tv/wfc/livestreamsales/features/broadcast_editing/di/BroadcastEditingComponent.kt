package tv.wfc.livestreamsales.features.broadcast_editing.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import tv.wfc.livestreamsales.features.broadcast_editing.di.module.ViewModelModule
import tv.wfc.livestreamsales.features.broadcast_editing.di.qualifer.BroadcastEditingFragment
import tv.wfc.livestreamsales.features.broadcast_editing.di.scope.BroadcastEditingFeatureScope

@BroadcastEditingFeatureScope
@Subcomponent(modules =[
    ViewModelModule::class
])
interface BroadcastEditingComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(
            @BindsInstance
            @BroadcastEditingFragment
            fragment: Fragment
        ) : BroadcastEditingComponent
    }

    fun inject(fragment: tv.wfc.livestreamsales.features.broadcast_editing.ui.BroadcastEditingFragment)
}