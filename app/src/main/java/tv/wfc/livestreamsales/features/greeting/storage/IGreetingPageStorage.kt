package tv.wfc.livestreamsales.features.greeting.storage

import tv.wfc.livestreamsales.features.greeting.model.GreetingPage
import io.reactivex.rxjava3.core.Single

interface IGreetingPageStorage {
    fun getGreetingPages(): Single<Set<GreetingPage>>
    fun getGreetingPage(index: Int): Single<GreetingPage>
}