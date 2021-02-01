package tv.wfc.livestreamsales.viewmodels.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tv.wfc.livestreamsales.application.errors.IApplicationErrorsLogger
import tv.wfc.livestreamsales.di.components.app.modules.reactivex.qualifiers.MainThreadScheduler
import tv.wfc.livestreamsales.repository.applicationsettings.IApplicationSettingsRepository
import tv.wfc.livestreamsales.repository.authorization.IAuthorizationRepository
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import javax.inject.Inject

class SplashViewModel @Inject constructor(
    private val authorizationRepository: IAuthorizationRepository,
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
                    authorizationRepository.isUserLoggedIn
                        .first(false)
                        .map { isUserLoggedIn ->
                            if(isUserLoggedIn){
                                ISplashViewModel.Destination.MAIN
                            } else ISplashViewModel.Destination.AUTHORIZATION
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