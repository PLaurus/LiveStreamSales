package com.example.livestreamsales.repository.greetingpage

import com.example.livestreamsales.model.application.greetingpage.GreetingPage
import io.reactivex.rxjava3.core.Single

interface IGreetingPageRepository {
    fun getGreetingPages(): Single<Set<GreetingPage>>
    fun getGreetingPage(index: Int): Single<GreetingPage>
}