package tv.wfc.livestreamsales.features.streamediting.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import org.joda.time.DateTime
import org.joda.time.LocalDate
import org.joda.time.LocalTime
import tv.wfc.contentloader.viewmodel.INeedPreparationViewModel
import tv.wfc.livestreamsales.features.streamediting.model.NextDestination

interface IStreamEditingViewModel: INeedPreparationViewModel {
    val isAnyOperationInProgress: LiveData<Boolean>

    val nextDestinationEvent: LiveData<NextDestination>

    val systemMessage: LiveData<String>

    val title: LiveData<String>

    val description: LiveData<String>

    val startDate: LiveData<LocalDate?>

    val startTime: LiveData<LocalTime?>

    val startDateTime: LiveData<DateTime?>

    val endDate: LiveData<LocalDate?>

    val endTime: LiveData<LocalTime?>

    val endDateTime: LiveData<DateTime?>

    val previewImage: LiveData<Uri?>

    val isStreamUpdateAvailable: LiveData<Boolean>

    fun prepare(streamId: Long)

    fun updateTitle(title: String)

    fun updateDescription(description: String)

    /**
     * Updates stream's start time.
     * @param dateInMillis date represented as the milliseconds from 1970-01-01T00:00:00Z
     */
    fun updateStartDate(dateInMillis: Long)

    /**
     * Updates stream's start time.
     * @param hour the hour of the day. If value is not in 0..23 range it will be adjusted.
     * @param minute the minute of the hour. If value is not in 0..59 range it will be adjusted.
     */
    fun updateStartTime(hour: Int, minute: Int)

    /**
     * Updates stream's end date.
     * @param dateInMillis date represented as the milliseconds from 1970-01-01T00:00:00Z
     */
    fun updateEndDate(dateInMillis: Long)

    /**
     * Updates stream's end time.
     * @param hour the hour of the day. If value is not in 0..23 range it will be adjusted.
     * @param minute the minute of the hour. If value is not in 0..59 range it will be adjusted.
     */
    fun updateEndTime(hour: Int, minute: Int)

    fun updatePreviewImage(uri: Uri?)

    fun updateStream()

    fun prepareToCloseCurrentDestination()

    fun prepareToNavigateToStartDatePickerDestination()

    fun prepareToNavigateToStartTimePickerDestination()

    fun prepareToNavigateToEndDatePickerDestination()

    fun prepareToNavigateToEndTimePickerDestination()

    fun prepareToNavigateToPreviewImagePickerDestination()

    fun prepareToNavigateToLiveBroadcastingDestination()
}