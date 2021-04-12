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

    protected val authorizedUserComponent by lazy{
        appComponent
            .authorizationRepository()
            .authorizedUserComponent
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setRootViewClickable()
    }

    private fun setRootViewClickable(){
        findViewById<ViewGroup>(android.R.id.content).getChildAt(0).apply {
            isClickable = true
            isFocusable = true
            isFocusableInTouchMode = true
        }
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN) {

            val currentFocus = currentFocus

            if (currentFocus is EditText) {
                val outRect = Rect()

                currentFocus.getGlobalVisibleRect(outRect)

                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    currentFocus.clearFocus()
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                    imm?.hideSoftInputFromWindow(currentFocus.windowToken, 0)
                }
            }
        }

        return super.dispatchTouchEvent(event)
    }
}