package tv.wfc.livestreamsales.application.tools.observer

import java.util.*

class MutableObservable<T>: IObserversManager<T> {
    private val observers = Vector<IObserver<T>>()

    override val observersCount: Int
        get() = observers.size

    override val hasObservers: Boolean
        get() = observers.size > 0

    @Synchronized
    override fun observe(observer: IObserver<T>) {
        if(observers.contains(observer)) return
        observers.addElement(observer)
    }

    @Synchronized
    override fun removeObserver(observer: IObserver<T>) {
        observers.removeElement(observer)
    }

    override fun removeObservers(observers: Collection<IObserver<T>>) {
        this.observers.removeAll(observers)
    }

    @Synchronized
    override fun clearObservers() {
        observers.removeAllElements()
    }

    override fun notifyObservers(newValue: T) {
        observers.forEach{ it.update(newValue) }
    }
}