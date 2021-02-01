package com.example.livestreamsales.repository.logout

import com.example.livestreamsales.di.components.app.subscomponents.authorizeduser.modules.storage.qualifiers.LogOutRemoteStorage
import com.example.livestreamsales.storage.logout.ILogOutStorage
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class LogOutRepository @Inject constructor(
    @LogOutRemoteStorage
    private val logOutRemoteStorage: ILogOutStorage
): ILogOutRepository {
    override fun logOut(): Completable = logOutRemoteStorage.logOut()
}