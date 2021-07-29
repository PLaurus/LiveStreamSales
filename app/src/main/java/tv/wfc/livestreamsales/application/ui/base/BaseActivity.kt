package tv.wfc.livestreamsales.application.ui.base

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.os.PersistableBundle
import android.view.MotionEvent
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import tv.wfc.livestreamsales.application.LiveStreamSalesApplication
import tv.wfc.livestreamsales.application.di.AppComponent

abstract class BaseActivity: AppCompatActivity() {
    protected val appComponent: AppComponent by lazy{
        (application as LiveStreamSalesApplication).appComponent
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setRootViewClickable()
    }

    private fun setRootViewClickable(){
        findViewById<ViewGroup>(android.R.id.content).apply {
            isClickable = true
            isFocusable = true
            isFocusableInTouchMode = true
        }
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        if(event?.action == MotionEvent.ACTION_UP){
            val previousFocusedView = currentFocus ?: return super.dispatchTouchEvent(event)
            val isTouchEventConsumedAfterAction = super.dispatchTouchEvent(event)
            val currentFocusedView = currentFocus ?: previousFocusedView

            if(currentFocusedView == previousFocusedView){
                val currentFocusedViewRect = Rect()
                currentFocusedView.getGlobalVisibleRect(currentFocusedViewRect)

                if(currentFocusedViewRect.contains(event.rawX.toInt(), event.rawY.toInt())){
                    return isTouchEventConsumedAfterAction
                } else{
                    currentFocusedView.clearFocus()
                }
            }
            else if(currentFocusedView is EditText){
                return isTouchEventConsumedAfterAction
            }

            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(currentFocusedView.windowToken, 0)

            return isTouchEventConsumedAfterAction
        }

        return super.dispatchTouchEvent(event)
    }
}