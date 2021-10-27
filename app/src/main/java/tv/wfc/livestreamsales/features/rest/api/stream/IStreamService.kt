package tv.wfc.livestreamsales.features.rest.api.stream

import io.reactivex.rxjava3.core.Single
import okhttp3.MultipartBody
import org.joda.time.DateTime
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import tv.wfc.livestreamsales.features.rest.api.base.IApi
import tv.wfc.livestreamsales.features.rest.api.stream.dto.StreamCreationResultDto
import tv.wfc.livestreamsales.features.rest.api.stream.dto.StreamUpdateResultDto

interface IStreamService: IApi {
    /**
     * Creates new stream on server side.
     * @param name stream name.
     * @param description stream description.
     * @param startAt date and time when stream comes online.
     * @param endAt date and time when stream goes offline.
     * @param image preview image of the stream.
     * @param videoWidth width of video.
     * @param videoHeight height of video.
     * @return full information about created stream and about connection.
     */
    @POST("stream")
    @Multipart
    fun createStream(
       @Part("name") name: String?,
       @Part("description") description: String?,
       @Part("start_at") startAt: DateTime?,
       @Part("end_at") endAt: DateTime?,
       @Part image: MultipartBody.Part?,
       @Part("aspect_ratio_width") videoWidth: Int?,
       @Part("aspect_ratio_height") videoHeight: Int?
    ): Single<StreamCreationResultDto>

    /**
     * Updates stream on server side.
     * @param streamId stream id.
     * @param name stream name.
     * @param description stream description.
     * @param startAt date and time when stream comes online.
     * @param endAt date and time when stream goes offline.
     * @param image preview image of the stream.
     * @param videoWidth width of video.
     * @param videoHeight height of video.
     * @param method should be used only when you really know what you are doing. In most
     * cases should be left as is.
     * @return full information about updated stream and about connection.
     */
    @POST("stream/{stream_id}")
    fun updateStream(
        @Path("stream_id") streamId: Long,
        @Part("name") name: String?,
        @Part("description") description: String?,
        @Part("start_at") startAt: DateTime?,
        @Part("end_at") endAt: DateTime?,
        @Part image: MultipartBody.Part?,
        @Part("aspect_ratio_width") videoWidth: Int?,
        @Part("aspect_ratio_height") videoHeight: Int?,
        @Part("_method") method: String? = "PATCH"
    ): Single<StreamUpdateResultDto>
}