package tv.wfc.livestreamsales.features.mystreams.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import tv.wfc.livestreamsales.features.mystreams.di.modules.diffutils.DiffUtilsModule
import tv.wfc.livestreamsales.features.mystreams.di.modules.viewmodel.ViewModelModule
import tv.wfc.livestreamsales.features.mystreams.di.qualifiers.MyStreamsFragment
import tv.wfc.livestreamsales.features.mystreams.di.scope.MyStreamsFeatureScope

@MyStreamsFeatureScope
@Subcomponent(modules = [
    ViewModelModule::class,
    DiffUtilsModule::class
])
interface MyStreamsComponent {
    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun fragment(
            @MyStreamsFragment
            fragment: Fragment
        ): Builder

        fun build(): MyStreamsComponent
    }

    fun inject(fragment: tv.wfc.livestreamsales.features.mystreams.ui.MyStreamsFragment)
}