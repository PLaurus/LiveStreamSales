package tv.wfc.livestreamsales.utils

import androidx.annotation.MainThread
import androidx.collection.arrayMapOf
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer

class LiveEvent<T>: MediatorLiveData<T>() {
    private val observers = arrayMapOf<Observer<in T>, ObserverWrapper<in T>>()

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        val wrapper = ObserverWrapper(observer)
        observers[observer] = wrapper
        super.observe(owner, wrapper)
    }

    @MainThread
    override fun observeForever(observer: Observer<in T>) {
        val wrapper = ObserverWrapper(observer)
        observers[observer] = wrapper
        super.observeForever(wrapper)
    }

    @MainThread
    override fun removeObserver(observer: Observer<in T>) {
        if (observers.remove(observer) != null) {
            super.removeObserver(observer)
            return
        }
    }

    @MainThread
    override fun setValue(t: T?) {
        observers.forEach {(_, wrapper) -> wrapper.notifyNewValueIncoming() }
        super.setValue(t)
    }

    private class ObserverWrapper<T>(val observer: Observer<T>) : Observer<T> {

        private var isBrandNewValueIncoming = false

        override fun onChanged(t: T?) {
            if (isBrandNewValueIncoming) {
                isBrandNewValueIncoming = false
                observer.onChanged(t)
            }
        }

        fun notifyNewValueIncoming() {
            isBrandNewValueIncoming = true
        }
    }
}