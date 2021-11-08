package tv.wfc.livestreamsales.application.datasource.mystream

import android.content.Context
import android.net.Uri
import com.laurus.p.tools.okhttp3.toMultipartBodyPart
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import okhttp3.MultipartBody
import org.joda.time.DateTime
import tv.wfc.core.entity.IEntityMapper
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.IoScheduler
import tv.wfc.livestreamsales.application.model.exception.storage.ReceivedDataWithWrongFormatException
import tv.wfc.livestreamsales.application.model.stream.MyStream
import tv.wfc.livestreamsales.features.rest.api.mystreams.IMyStreamsService
import tv.wfc.livestreamsales.features.rest.api.mystreams.dto.MyStreamsDto
import tv.wfc.livestreamsales.features.rest.api.stream.IStreamService
import tv.wfc.livestreamsales.features.rest.api.stream.dto.StreamCreationResultDto
import tv.wfc.livestreamsales.features.rest.api.stream.dto.StreamUpdateResultDto
import javax.inject.Inject

class MyStreamDataSource @Inject constructor(
    private val streamService: IStreamService,
    private val myStreamsService: IMyStreamsService,
    private val applicationContext: Context,
    private val streamCreationResultDtoToMyStreamMapper: IEntityMapper<StreamCreationResultDto, MyStream>,
    private val streamUpdateResultDtoToMyStreamMapper: IEntityMapper<StreamUpdateResultDto, MyStream>,
    private val myStreamsDtoToListOfMyStreamsMapper: IEntityMapper<MyStreamsDto, List<MyStream>>,
    @IoScheduler
    private val ioScheduler: Scheduler
) : IMyStreamDataSource {
    override fun createMyStream(
        name: String,
        description: String,
        startAt: DateTime,
        endAt: DateTime,
        videoWidth: Int,
        videoHeight: Int,
        image: Uri?
    ): Single<MyStream> {
        return Single
            .defer {
                val imagePart: MultipartBody.Part? = image?.toMultipartBodyPart(
                    applicationContext,
                    "image"
                )

                streamService.createStream(
                    name,
                    description,
                    startAt,
                    endAt,
                    imagePart,
                    videoWidth,
                    videoHeight
                )
            }
            .map {
                streamCreationResultDtoToMyStreamMapper.map(it)
                    ?: throw ReceivedDataWithWrongFormatException()
            }
            .subscribeOn(ioScheduler)
    }

    override fun updateMyStream(
        streamId: Long,
        name: String,
        description: String,
        startAt: DateTime,
        endAt: DateTime,
        videoWidth: Int,
        videoHeight: Int,
        image: Uri?
    ): Single<MyStream> {
        return Single
            .defer {
                val imagePart: MultipartBody.Part? = image
                    ?.toMultipartBodyPart(applicationContext, "image")

                streamService.updateStream(
                    streamId,
                    name,
                    description,
                    startAt,
                    endAt,
                    imagePart,
                    videoWidth,
                    videoHeight
                )
            }
            .map {
                streamUpdateResultDtoToMyStreamMapper.map(it)
                    ?: throw ReceivedDataWithWrongFormatException()
            }
            .subscribeOn(ioScheduler)
    }

    override fun getMyStreams(): Single<List<MyStream>> {
        return myStreamsService
            .getMyStreams()
            .map {
                myStreamsDtoToListOfMyStreamsMapper.map(it)
                    ?: throw ReceivedDataWithWrongFormatException()
            }
            .subscribeOn(ioScheduler)
    }
}