package tv.wfc.livestreamsales.application.tools.observer

interface IObservable<T> {
    /**
     * Count of subscribed observers.
     */
    val observersCount: Int

    /**
     * True - when there is one or more subscribed observers; False - when there are no subscribed
     * observers.
     */
    val hasObservers: Boolean

    /**
     * Adds an observer to the set of observers for this object, provided
     * that it is not the same as some observer already in the set.
     * The order in which notifications will be delivered to multiple
     * observers is not specified. See the class comment.
     *
     * @param observer an observer to be added.
     */
    fun observe(observer: IObserver<T>)

    /**
     * Removes an observer from the set of observers of this object.
     * @param observer the observer to be removed.
     */
    fun removeObserver(observer: IObserver<T>)

    /**
     * Removes the [observers] from the set of observers of this object.
     * @param observers the observers to be removed.
     */
    fun removeObservers(observers: Collection<IObserver<T>>)

    /**
     * Clears the observer list so that this object has not any observers.
     */
    fun clearObservers()
}