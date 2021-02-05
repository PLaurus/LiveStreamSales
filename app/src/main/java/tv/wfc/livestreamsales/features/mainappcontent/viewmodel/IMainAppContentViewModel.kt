package tv.wfc.livestreamsales.features.mainappcontent.viewmodel

import androidx.lifecycle.LiveData

interface IMainAppContentViewModel {
    val isUserLoggedIn: LiveData<Boolean>
}