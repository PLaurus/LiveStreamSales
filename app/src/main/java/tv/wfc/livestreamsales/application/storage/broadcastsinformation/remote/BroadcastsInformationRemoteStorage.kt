package tv.wfc.livestreamsales.application.storage.broadcastsinformation.remote

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import org.joda.time.DateTime
import tv.wfc.livestreamsales.BuildConfig
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.IoScheduler
import tv.wfc.livestreamsales.application.model.broadcastinformation.BroadcastInformation
import tv.wfc.livestreamsales.features.rest.api.notauthorized.IBroadcastsInformationApi
import tv.wfc.livestreamsales.application.storage.broadcastsinformation.IBroadcastsInformationStorage
import javax.inject.Inject

class BroadcastsInformationRemoteStorage @Inject constructor(
    private val broadcastsInformationApi: IBroadcastsInformationApi,
    @IoScheduler
    private val ioScheduler: Scheduler
): IBroadcastsInformationStorage {
    override fun getBroadcasts(): Single<List<BroadcastInformation>> {
        if(BuildConfig.DEBUG && BuildConfig.IgnoreRestRequests){
            return Single.just(listOf(
                BroadcastInformation(
                    0,
                    "Отдай мне свои деньги 1",
                    "Цыган вымогает деньги 1",
                    DateTime.now().minusDays(2),
                    ""
                ),
                BroadcastInformation(
                    0,
                    "Отдай мне свои деньги 2",
                    "Цыган вымогает деньги 2",
                    DateTime.now().minusDays(1),
                    "https://www.creativefabrica.com/wp-content/uploads/2019/03/Monogram-YA-Logo-Design-by-Greenlines-Studios.jpg"
                ),
                BroadcastInformation(
                    0,
                    "Отдай мне свои деньги 3",
                    "Цыган вымогает деньги 3",
                    DateTime.now().plusDays(1),
                    "https://www.creativefabrica.com/wp-content/uploads/2019/03/Monogram-YA-Logo-Design-by-Greenlines-Studios.jpg"
                ),
                BroadcastInformation(
                    0,
                    "Отдай мне свои деньги 4",
                    "Цыган вымогает деньги 4",
                    DateTime.now().plusDays(2),
                )
            ))
        }

        return broadcastsInformationApi
            .getBroadcasts()
            .flatMap{ response ->
                val broadcasts = response.body()?.data

                if(broadcasts != null){
                    Single.just(broadcasts)
                } else{
                    throw Exception("Failed to receive broadcasts from server!")
                }
            }
            .subscribeOn(ioScheduler)
    }

    override fun saveBroadcasts(broadcasts: List<BroadcastInformation>): Completable {
        return Completable.create {
            it.onError(NotImplementedError())
        }
    }

    override fun getBroadcast(id: Long): Single<BroadcastInformation> {
        if(BuildConfig.DEBUG && BuildConfig.IgnoreRestRequests){
            return Single.just(BroadcastInformation(
                0,
                "Отдай мне свои деньги 2",
                "Цыган вымогает деньги 2",
                DateTime.now().minusDays(1),
                "https://www.creativefabrica.com/wp-content/uploads/2019/03/Monogram-YA-Logo-Design-by-Greenlines-Studios.jpg",
                "https://video-weaver.fra05.hls.ttvnw.net/v1/playlist/CvEE7R0LyaxBOS9AGIZZAi6wscITXH0cYBBD6uBbt7Ha4XJl7CS6vcwMh9mHn5J0jK2N04HYbg8j9BQKJSqx2FFZCwCXWPszJIAaAbCD4cxJ11z88hsDXndVOxzMaSy9uWnLhT39FZJnqmIQ0Qq-s2awlBkoJCLzx7AYDUUj3_HGH2Nbcx4TYaAYLG72sNvupmls89KIAoVrWhOUn5MBADYheFqh5folsayjb9rDpMayCuH1RE9zwaSllNNGSTyiLXS1Gg3FKR83l5nl2oAnsw0nEGjxGLvMNj0--RwRl7DgRvrSl6NAO0AJ0a8zl4yDcKJEgdaIg3urjb6Lo2UyzPcgYhE4jG8nMYGX7plMY9cytXJYrhDXlurPTELSLL4tOuA4_OTKF5boNh5BzqtTN4QKh9e2dngUnZDKVqARSr-xcn-ts2hsy0ud_plh9P7_DHXMjJT6C7yfLkAnFEhIGzQexvGSZ9LhGOgYSjDuOs3Fsjh6biuaAHRV94YHxH-oe4rKon2mfSTRBCJ5u8e03ghikLPKsmc7PADV7LWD-5JMSLCseeLJAP7zbxZqv778tii-UcatwnisRrZkeFBmClQlTiSj6ux_HSK3ZKmMSWFLfl5bp0OgkY531yq9_Pokmlq4JmVYitbQgsnMlm0CVO6JsFLWQhxQQkNjmJ3gGC6gCX7DWoPHMSiA5t6cfypY56kn9vFDKWgUop6kppqQPLSkNBTYV29UgcRoKhb_IU9WO11bGFlljtat9WWTSJ0WEi25Zcul7VR__-l0DhB5RyBcUPli55k7DGuEX3pFb1PsgRUlBbqxZRZPafplwjmrtIiefRIQvqXxzIhw5I7H_5F7EyG7bRoMOrx5zvB4ZUjVDuzF.m3u8",
                8
            ))
        }

        return broadcastsInformationApi
            .getBroadcast(id)
            .flatMap { response ->
                val broadcast = response.body()?.data

                if(broadcast != null){
                    Single.just(broadcast)
                } else{
                    throw Exception("Failed to receive broadcast from server!")
                }
            }
            .subscribeOn(ioScheduler)
    }
}