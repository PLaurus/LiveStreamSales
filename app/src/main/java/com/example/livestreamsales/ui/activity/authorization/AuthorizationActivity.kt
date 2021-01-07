package com.example.livestreamsales.ui.activity.authorization

import androidx.navigation.findNavController
import com.example.livestreamsales.R
import com.example.livestreamsales.ui.activity.base.BaseActivity

class AuthorizationActivity: BaseActivity() {
    private val navigationController by lazy{
        findNavController(R.id.authorization_navigation_host_fragment)
    }
}