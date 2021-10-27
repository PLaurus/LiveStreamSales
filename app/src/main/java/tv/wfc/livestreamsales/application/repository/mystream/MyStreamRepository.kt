package tv.wfc.livestreamsales.application.repository.mystream

import android.net.Uri
import io.reactivex.rxjava3.core.Single
import org.joda.time.DateTime
import tv.wfc.livestreamsales.application.datasource.mystream.IMyStreamDataSource
import tv.wfc.livestreamsales.application.model.stream.MyStream
import javax.inject.Inject

class MyStreamRepository @Inject constructor(
    private val myStreamDataSource: IMyStreamDataSource
): IMyStreamRepository {
    override fun createMyStream(
        name: String,
        description: String,
        startAt: DateTime,
        endAt: DateTime,
        videoWidth: Int,
        videoHeight: Int,
        image: Uri?
    ): Single<MyStream> = myStreamDataSource
        .createMyStream(name, description, startAt, endAt, videoWidth, videoHeight, image)

    override fun updateMyStream(
        streamId: Long,
        name: String,
        description: String,
        startAt: DateTime,
        endAt: DateTime,
        videoWidth: Int,
        videoHeight: Int,
        image: Uri?
    ): Single<MyStream> = myStreamDataSource
        .updateMyStream(streamId, name, description, startAt, endAt, videoWidth, videoHeight, image)

    override fun getMyStreams(): Single<List<MyStream>> = myStreamDataSource.getMyStreams()
}