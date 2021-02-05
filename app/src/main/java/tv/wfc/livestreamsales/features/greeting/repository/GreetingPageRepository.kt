package tv.wfc.livestreamsales.features.greeting.repository

import tv.wfc.livestreamsales.features.greeting.di.modules.localstorage.qualifiers.GreetingPageLocalStorage
import tv.wfc.livestreamsales.features.greeting.model.GreetingPage
import tv.wfc.livestreamsales.features.greeting.storage.IGreetingPageStorage
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class GreetingPageRepository @Inject constructor(
    @GreetingPageLocalStorage
    private val greetingPageLocalStorage: IGreetingPageStorage
): IGreetingPageRepository {
    override fun getGreetingPages(): Single<Set<GreetingPage>> {
        return greetingPageLocalStorage.getGreetingPages()
    }

    override fun getGreetingPage(index: Int): Single<GreetingPage> {
        return greetingPageLocalStorage.getGreetingPage(index)
    }
}