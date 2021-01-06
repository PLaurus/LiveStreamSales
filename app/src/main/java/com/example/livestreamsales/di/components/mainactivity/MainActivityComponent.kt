package com.example.livestreamsales.di.components.mainactivity

import androidx.lifecycle.ViewModelStoreOwner
import com.example.livestreamsales.di.scopes.ActivityScope
import com.example.livestreamsales.ui.activity.main.MainActivity
import dagger.BindsInstance
import dagger.Subcomponent
import javax.inject.Named

@ActivityScope
@Subcomponent(modules = [
    ViewModelModule::class
])
interface MainActivityComponent {

    companion object{
        internal const val DEPENDENCY_NAME_MAIN_VIEW_MODEL_STORE_OWNER = "MAIN_VIEW_MODEL_STORE_OWNER"
    }

    @Subcomponent.Factory
    interface Factory{
        fun create(
            @BindsInstance
            @Named(DEPENDENCY_NAME_MAIN_VIEW_MODEL_STORE_OWNER)
            lifecycleOwner: ViewModelStoreOwner
        ): MainActivityComponent
    }

    fun inject(activity: MainActivity)
}