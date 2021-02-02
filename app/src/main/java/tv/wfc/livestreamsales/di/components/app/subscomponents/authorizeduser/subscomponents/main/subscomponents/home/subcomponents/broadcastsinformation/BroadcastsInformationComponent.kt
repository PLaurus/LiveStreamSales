package tv.wfc.livestreamsales.di.components.app.subscomponents.authorizeduser.subscomponents.main.subscomponents.home.subcomponents.broadcastsinformation

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import tv.wfc.livestreamsales.di.components.app.subscomponents.authorizeduser.subscomponents.main.subscomponents.home.subcomponents.broadcastsinformation.modules.viewmodel.BroadcastsInformationViewModelModule
import tv.wfc.livestreamsales.di.components.app.subscomponents.authorizeduser.subscomponents.main.subscomponents.home.subcomponents.broadcastsinformation.qualifiers.BroadcastsInformationFragment
import tv.wfc.livestreamsales.di.components.app.subscomponents.authorizeduser.subscomponents.main.subscomponents.home.subcomponents.broadcastsinformation.qualifiers.BroadcastsInformationFragmentScope

@BroadcastsInformationFragmentScope
@Subcomponent(modules = [
    BroadcastsInformationViewModelModule::class
])
interface BroadcastsInformationComponent {
    @Subcomponent.Factory
    interface Factory{
        fun create(
            @BindsInstance
            @BroadcastsInformationFragment
            fragment: Fragment
        ): BroadcastsInformationComponent
    }

    fun inject(fragment: tv.wfc.livestreamsales.ui.activity.main.fragments.home.fragments.BroadcastsInformationFragment)
}