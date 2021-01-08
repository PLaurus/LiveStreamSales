package com.example.livestreamsales.ui.fragment.base

import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.example.livestreamsales.ui.activity.authorization.AuthorizationActivity

abstract class AuthorizationFragment(@LayoutRes contentLayoutId: Int): Fragment(contentLayoutId) {
    protected val authorizationActivity by lazy{
        (requireActivity() as AuthorizationActivity)
    }
    protected val authorizationComponent by lazy{
        authorizationActivity.authorizationComponent
    }
}