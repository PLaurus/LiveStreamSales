package tv.wfc.livestreamsales.application.tools.observer

interface IObserversManager<T>: IObservable<T> {
    fun notifyObservers(newValue: T)
}