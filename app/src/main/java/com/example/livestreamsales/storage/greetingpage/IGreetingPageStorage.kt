package com.example.livestreamsales.storage.greetingpage

import com.example.livestreamsales.model.application.greetingpage.GreetingPage
import io.reactivex.rxjava3.core.Single

interface IGreetingPageStorage {
    fun getGreetingPages(): Single<Set<GreetingPage>>
    fun getGreetingPage(index: Int): Single<GreetingPage>
}