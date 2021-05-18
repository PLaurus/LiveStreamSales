package tv.wfc.livestreamsales.features.splash.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.MainThreadScheduler
import tv.wfc.livestreamsales.application.manager.IAuthorizationManager
import tv.wfc.livestreamsales.application.repository.applicationsettings.IApplicationSettingsRepository
import tv.wfc.livestreamsales.application.tools.errors.IApplicationErrorsLogger
import javax.inject.Inject

class SplashViewModel @Inject constructor(
    private val authorizationManager: IAuthorizationManager,
    private val applicationSettingsRepository: IApplicationSettingsRepository,
    @MainThreadScheduler
    private val mainThreadScheduler: Scheduler,
    private val applicationErrorsLogger: IApplicationErrorsLogger
): ViewModel(), ISplashViewModel {
    private val disposables = CompositeDisposable()

    override val nextDestination: LiveData<ISplashViewModel.Destination> = MutableLiveData<ISplashViewModel.Destination>().apply{
        getNextDestination()
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onSuccess = ::setValue,
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }

    private fun getNextDestination(): Single<ISplashViewModel.Destination>{
        return applicationSettingsRepository.getIsGreetingShown()
            .flatMap { isGreetingShown ->
                if(isGreetingShown){
                    authorizationManager.isUserLoggedIn
                        .first(false)
                        .map { isUserLoggedIn ->
                            if(isUserLoggedIn){
                                ISplashViewModel.Destination.MAIN_APP_CONTENT
                            } else ISplashViewModel.Destination.LOG_IN
                        }
                } else{
                    Single.just(ISplashViewModel.Destination.GREETING)
                }
            }
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}