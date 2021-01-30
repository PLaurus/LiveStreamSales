package com.example.livestreamsales.viewmodels.greeting

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.livestreamsales.application.errors.IApplicationErrorsLogger
import com.example.livestreamsales.di.components.app.modules.reactivex.qualifiers.MainThreadScheduler
import com.example.livestreamsales.model.application.greetingpage.GreetingPage
import com.example.livestreamsales.repository.applicationsettings.IApplicationSettingsRepository
import com.example.livestreamsales.repository.authorization.IAuthorizationRepository
import com.example.livestreamsales.repository.greetingpage.IGreetingPageRepository
import com.example.livestreamsales.utils.LiveEvent
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import javax.inject.Inject

class GreetingViewModel @Inject constructor(
    applicationSettingsRepository: IApplicationSettingsRepository,
    private val greetingPageRepository: IGreetingPageRepository,
    authorizationRepository: IAuthorizationRepository,
    @MainThreadScheduler
    private val mainThreadScheduler: Scheduler,
    private val applicationErrorsLogger: IApplicationErrorsLogger
): ViewModel(), IGreetingViewModel {
    private val disposables = CompositeDisposable()

    private val isUserLoggedInSingle =
        authorizationRepository
            .isUserLoggedIn
            .first(false)

    private val saveGreetingIsShownSingle =
        applicationSettingsRepository
            .saveIsGreetingShown(true)
            .toSingleDefault(Unit)

    override val greetingPages = MutableLiveData<Set<GreetingPage>>().apply {
        greetingPageRepository
            .getGreetingPages()
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onSuccess = ::setValue,
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }

    override val nextDestinationEvent = LiveEvent<IGreetingViewModel.Destination>()

    private var notifyGreetingIsShownDisposable: Disposable? = null

    override fun notifyGreetingIsShown() {
        notifyGreetingIsShownDisposable?.dispose()
        notifyGreetingIsShownDisposable = Single
            .zip(isUserLoggedInSingle, saveGreetingIsShownSingle){ isUserLoggedIn, _ ->
                getNextDestination(isUserLoggedIn)
            }
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onSuccess = nextDestinationEvent::setValue,
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }

    private fun getNextDestination(isUserLoggedIn: Boolean): IGreetingViewModel.Destination{
        return if(isUserLoggedIn){
            IGreetingViewModel.Destination.MAIN
        } else{
            IGreetingViewModel.Destination.AUTHORIZATION
        }
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}