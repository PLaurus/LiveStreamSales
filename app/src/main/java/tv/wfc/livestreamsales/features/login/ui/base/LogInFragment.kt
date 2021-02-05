package tv.wfc.livestreamsales.features.login.ui.base

import androidx.annotation.LayoutRes
import tv.wfc.livestreamsales.features.login.ui.LogInActivity
import tv.wfc.livestreamsales.application.ui.base.BaseFragment

abstract class LogInFragment(@LayoutRes contentLayoutId: Int): BaseFragment(contentLayoutId) {
    protected val logInActivity by lazy{
        requireActivity() as LogInActivity
    }

    protected val logInComponent by lazy{
        logInActivity.logInComponent
    }
}