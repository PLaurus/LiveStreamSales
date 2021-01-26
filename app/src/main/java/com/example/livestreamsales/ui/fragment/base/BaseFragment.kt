package com.example.livestreamsales.ui.fragment.base

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.livestreamsales.R
import com.example.livestreamsales.databinding.FragmentBaseBinding
import io.reactivex.rxjava3.disposables.CompositeDisposable

abstract class BaseFragment(
    @LayoutRes
    private val contentLayoutId: Int
): Fragment() {
    protected lateinit var viewScopeDisposables: CompositeDisposable
    private var viewBinding: FragmentBaseBinding? = null

    @CallSuper
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewScopeDisposables = CompositeDisposable()

        val view = bindView(inflater, container)

        inflateContentView(view.context)

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewScopeDisposables.dispose()
        unbindView()
    }

    private fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): View{
        val viewBinding = FragmentBaseBinding.inflate(inflater, parent, false)
        this.viewBinding = viewBinding
        return viewBinding.root.apply{
            replaceEmptyBackground()
        }
    }

    protected fun showContent(){
        viewBinding?.apply{
            contentContainer.visibility = View.VISIBLE
            progressContainer.visibility = View.INVISIBLE
            contentIsNotLoadedMessage.visibility = View.INVISIBLE
        }
    }

    protected fun showContentLoadingProgress(){
        viewBinding?.apply{
            contentContainer.visibility = View.INVISIBLE
            contentIsNotLoadedMessage.visibility = View.INVISIBLE
            progressContainer.apply{
                setBackgroundColor(Color.TRANSPARENT)
                visibility = View.VISIBLE
            }
        }
    }

    protected fun showOperationProgress(){
        viewBinding?.apply{
            contentIsNotLoadedMessage.visibility = View.INVISIBLE
            contentContainer.visibility = View.VISIBLE
            progressContainer.apply{
                setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.baseFragment_progressContainer_background
                    )
                )
                visibility = View.VISIBLE
            }
        }
    }

    protected fun showContentLoadingError(
        error: String = resources.getString(R.string.base_fragment_content_loading_error)
    ){
        viewBinding?.apply {
            contentContainer.visibility = View.INVISIBLE
            progressContainer.visibility = View.INVISIBLE
            contentIsNotLoadedMessage.apply{
                text = error
                visibility = View.VISIBLE
            }
        }
    }

    protected fun hideProgress(){
        viewBinding?.progressContainer?.visibility = View.INVISIBLE
    }

    private fun inflateContentView(context: Context): View?{
        var contentView: View? = null

        viewBinding?.contentContainer?.let{
            contentView = View.inflate(context, contentLayoutId, it).apply{
                layoutParams.apply {
                    width = ViewGroup.LayoutParams.MATCH_PARENT
                    height = ViewGroup.LayoutParams.MATCH_PARENT
                }
                requestLayout()
            }
        }

        return contentView
    }

    private fun unbindView(){
        viewBinding = null
    }

    private fun View.replaceEmptyBackground(){
        if(background == null){
            setDefaultBackground()
        }
    }

    private fun View.setDefaultBackground(){
        setBackgroundColor(ContextCompat.getColor(context, R.color.defaultFragmentBackground))
    }
}