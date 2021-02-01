package com.example.livestreamsales.repository.greetingpage

import com.example.livestreamsales.di.components.app.subscomponents.greeting.modules.localstorage.qualifiers.GreetingPageLocalStorage
import com.example.livestreamsales.model.application.greetingpage.GreetingPage
import com.example.livestreamsales.storage.greetingpage.IGreetingPageStorage
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