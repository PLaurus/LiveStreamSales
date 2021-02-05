package tv.wfc.livestreamsales.features.authorizeduser.ui.base

import android.content.Context
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import tv.wfc.livestreamsales.features.authorizeduser.di.AuthorizedUserComponent
import tv.wfc.livestreamsales.application.ui.base.BaseFragment

abstract class AuthorizedUserFragment(
    @LayoutRes
    contentLayoutId: Int
): BaseFragment(contentLayoutId){
    protected lateinit var authorizedUserComponent: AuthorizedUserComponent
        private set

    @CallSuper
    override fun onAttach(context: Context) {
        super.onAttach(context)
        initializeAuthorizedUserComponent()
    }

    private fun initializeAuthorizedUserComponent(){
        authorizedUserComponent = appComponent
            .authorizationRepository()
            .authorizedUserComponent
            ?: throw IllegalStateException("User MUST BE authorized to access ${this::class.qualifiedName} class!")
    }
}