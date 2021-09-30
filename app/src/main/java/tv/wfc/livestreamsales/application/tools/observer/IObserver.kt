package tv.wfc.livestreamsales.application.tools.observer

interface IObserver<T> {
    /**
     * This method is called whenever the observed object data is changed.
     * @param newValue is passed from observed object.
     */
    fun update(newValue: T)
}