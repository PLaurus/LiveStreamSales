package tv.wfc.livestreamsales.repository.greetingpage

import tv.wfc.livestreamsales.model.application.greetingpage.GreetingPage
import io.reactivex.rxjava3.core.Single

interface IGreetingPageRepository {
    fun getGreetingPages(): Single<Set<GreetingPage>>
    fun getGreetingPage(index: Int): Single<GreetingPage>
}