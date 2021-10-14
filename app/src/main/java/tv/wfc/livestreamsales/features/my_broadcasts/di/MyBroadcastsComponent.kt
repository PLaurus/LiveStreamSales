package tv.wfc.livestreamsales.features.my_broadcasts.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import tv.wfc.livestreamsales.features.my_broadcasts.di.modules.ViewModelModule
import tv.wfc.livestreamsales.features.my_broadcasts.di.qualifiers.MyBroadcastsFragment
import tv.wfc.livestreamsales.features.my_broadcasts.di.scope.MyBroadcastsFeatureScope

@MyBroadcastsFeatureScope
@Subcomponent(modules = [
    ViewModelModule::class
])
interface MyBroadcastsComponent {
    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun fragment(
            @MyBroadcastsFragment
            fragment: Fragment
        ): Builder

        fun build(): MyBroadcastsComponent
    }

    fun inject(fragment: tv.wfc.livestreamsales.features.my_broadcasts.ui.MyBroadcastsFragment)
}