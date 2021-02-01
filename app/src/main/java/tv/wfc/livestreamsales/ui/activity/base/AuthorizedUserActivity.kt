package tv.wfc.livestreamsales.ui.activity.base

abstract class AuthorizedUserActivity: BaseActivity() {
    protected val authorizedUserComponent by lazy{
        appComponent
            .authorizationRepository()
            .authorizedUserComponent ?:
            throw IllegalStateException("User MUST BE authorized to access ${this::class.qualifiedName} class!")
    }
}