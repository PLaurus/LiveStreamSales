package tv.wfc.livestreamsales.features.authorizeduser.ui.base

import tv.wfc.livestreamsales.application.ui.base.BaseActivity

abstract class AuthorizedUserActivity: BaseActivity() {
    protected val authorizedUserComponent by lazy{
        appComponent
            .authorizationRepository()
            .authorizedUserComponent ?:
            throw IllegalStateException("User MUST BE authorized to access ${this::class.qualifiedName} class!")
    }
}