package tv.wfc.livestreamsales.di.components.app.subscomponents.authorizeduser.subscomponents.main.subscomponents.broadcastsinformation

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import tv.wfc.livestreamsales.di.components.app.subscomponents.authorizeduser.subscomponents.main.subscomponents.broadcastsinformation.modules.api.ApiModule
import tv.wfc.livestreamsales.di.components.app.subscomponents.authorizeduser.subscomponents.main.subscomponents.broadcastsinformation.modules.repository.RepositoryModule
import tv.wfc.livestreamsales.di.components.app.subscomponents.authorizeduser.subscomponents.main.subscomponents.broadcastsinformation.modules.storage.StorageModule
import tv.wfc.livestreamsales.di.components.app.subscomponents.authorizeduser.subscomponents.main.subscomponents.broadcastsinformation.modules.viewmodel.BroadcastsInformationViewModelModule
import tv.wfc.livestreamsales.di.components.app.subscomponents.authorizeduser.subscomponents.main.subscomponents.broadcastsinformation.qualifiers.BroadcastsInformationFragment
import tv.wfc.livestreamsales.di.components.app.subscomponents.authorizeduser.subscomponents.main.subscomponents.broadcastsinformation.qualifiers.BroadcastsInformationFragmentScope

@BroadcastsInformationFragmentScope
@Subcomponent(modules = [
    BroadcastsInformationViewModelModule::class,
    ApiModule::class,
    StorageModule::class,
    RepositoryModule::class
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