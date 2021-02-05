package tv.wfc.livestreamsales.features.greeting.repository

import tv.wfc.livestreamsales.features.greeting.model.GreetingPage
import io.reactivex.rxjava3.core.Single

interface IGreetingPageRepository {
    fun getGreetingPages(): Single<Set<GreetingPage>>
    fun getGreetingPage(index: Int): Single<GreetingPage>
}