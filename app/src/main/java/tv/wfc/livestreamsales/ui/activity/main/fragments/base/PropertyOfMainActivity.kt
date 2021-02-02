package tv.wfc.livestreamsales.ui.activity.main.fragments.base

import androidx.annotation.LayoutRes
import tv.wfc.livestreamsales.ui.activity.main.MainActivity
import tv.wfc.livestreamsales.ui.fragment.base.BaseFragment

abstract class PropertyOfMainActivity(
    @LayoutRes
    contentLayoutId: Int
): BaseFragment(contentLayoutId){
    protected val mainActivity by lazy{
        requireActivity() as MainActivity
    }

    protected val mainComponent by lazy{
        mainActivity.mainComponent
    }
}