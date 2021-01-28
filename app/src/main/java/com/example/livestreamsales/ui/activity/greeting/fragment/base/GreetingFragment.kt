package com.example.livestreamsales.ui.activity.greeting.fragment.base

import androidx.annotation.LayoutRes
import com.example.livestreamsales.ui.activity.greeting.GreetingActivity
import com.example.livestreamsales.ui.fragment.base.BaseFragment

abstract class GreetingFragment(@LayoutRes contentLayoutId: Int): BaseFragment(contentLayoutId){
    private val greetingActivity by lazy{
        requireActivity() as GreetingActivity
    }

    private val greetingComponent by lazy{
        greetingActivity.greetingComponent
    }
}