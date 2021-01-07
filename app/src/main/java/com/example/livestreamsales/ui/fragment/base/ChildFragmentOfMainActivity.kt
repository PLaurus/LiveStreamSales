package com.example.livestreamsales.ui.fragment.base

import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment

abstract class ChildFragmentOfMainActivity(
    @LayoutRes
    contentLayoutId: Int
): Fragment(contentLayoutId)