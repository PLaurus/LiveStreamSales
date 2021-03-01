package tv.wfc.livestreamsales.features.greeting.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.laurus.p.tools.livedata.LiveEvent
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.MainThreadScheduler
import tv.wfc.livestreamsales.application.repository.applicationsettings.IApplicationSettingsRepository
import tv.wfc.livestreamsales.application.repository.authorization.IAuthorizationRepository
import tv.wfc.livestreamsales.application.tools.errors.IApplicationErrorsLogger
import tv.wfc.livestreamsales.features.greeting.model.GreetingPage
import tv.wfc.livestreamsales.features.greeting.repository.IGreetingPageRepository
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

    private fun getNextDestination(isUserLoggedIn: Boolean): IGreetingViewModel.Destination {
        return if(isUserLoggedIn){
            IGreetingViewModel.Destination.MAIN_APP_CONTENT
        } else{
            IGreetingViewModel.Destination.LOG_IN
        }
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}