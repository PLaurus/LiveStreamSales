package tv.wfc.livestreamsales.ui.activity.main.fragments.home.fragments.base

import androidx.annotation.LayoutRes
import tv.wfc.livestreamsales.ui.activity.main.fragments.home.HomeFragment
import tv.wfc.livestreamsales.ui.fragment.base.BaseFragment

abstract class PropertyOfHomeFragment(
    @LayoutRes contentLayoutId: Int
): BaseFragment(contentLayoutId) {
    protected val homeFragment by lazy{
        requireParentFragment() as HomeFragment
    }

    protected val homeComponent by lazy{
        homeFragment.homeComponent
    }
}