package tv.wfc.livestreamsales.ui.activity.login.fragments.base

import androidx.annotation.LayoutRes
import tv.wfc.livestreamsales.ui.activity.login.LogInActivity
import tv.wfc.livestreamsales.ui.fragment.base.BaseFragment

abstract class LogInFragment(@LayoutRes contentLayoutId: Int): BaseFragment(contentLayoutId) {
    protected val authorizationActivity by lazy{
        requireActivity() as LogInActivity
    }

    protected val authorizationComponent by lazy{
        authorizationActivity.logInComponent
    }
}