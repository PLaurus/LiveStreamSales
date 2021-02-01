package com.example.livestreamsales.ui.activity.main.fragments.base

import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment

abstract class MainFragment(
    @LayoutRes
    contentLayoutId: Int
): Fragment(contentLayoutId)