package tv.wfc.livestreamsales.features.streamcreation.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.laurus.p.tools.camera.ICameraResolutionsProvider
import com.laurus.p.tools.camera.model.AspectRatio
import com.laurus.p.tools.camera.model.Resolution
import com.laurus.p.tools.livedata.LiveEvent
import com.laurus.p.tools.reactivex.NullablesWrapper
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableSource
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.functions.BiFunction
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import org.joda.time.DateTime
import org.joda.time.LocalDate
import org.joda.time.LocalTime
import tv.wfc.contentloader.model.ViewModelPreparationState
import tv.wfc.livestreamsales.R
import tv.wfc.livestreamsales.application.base.viewmodel.BaseViewModel
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.MainThreadScheduler
import tv.wfc.livestreamsales.application.repository.mystream.IMyStreamRepository
import tv.wfc.livestreamsales.application.tools.errors.IApplicationErrorsLogger
import tv.wfc.livestreamsales.features.streamcreation.model.NextDestination
import javax.inject.Inject

@SuppressLint("StaticFieldLeak")
class StreamCreationViewModel @Inject constructor(
    private val applicationContext: Context,
    private val cameraResolutionsProvider: ICameraResolutionsProvider,
    private val myStreamRepository: IMyStreamRepository,
    @MainThreadScheduler
    private val mainThreadScheduler: Scheduler,
    private val applicationErrorsLogger: IApplicationErrorsLogger
) : BaseViewModel(), IStreamCreationViewModel {
    private val dataPreparationStateSubject =
        BehaviorSubject.createDefault<ViewModelPreparationState>(ViewModelPreparationState.DataIsPrepared)

    private val nextDestinationEventSubject = PublishSubject.create<NextDestination>()

    private val systemMessageSubject = PublishSubject.create<String>()

    private val titleSubject = BehaviorSubject.createDefault(NullablesWrapper<String>(null))

    private val descriptionSubject = BehaviorSubject.createDefault(NullablesWrapper<String>(null))

    private val startDateSubject = BehaviorSubject.createDefault(NullablesWrapper<LocalDate>(null))

    private val startTimeSubject = BehaviorSubject.createDefault(NullablesWrapper<LocalTime>(null))

    private val startDateTimeSubject = startDateSubject.combineWithTimeAsSubject(startTimeSubject)

    private val endDateSubject = BehaviorSubject.createDefault(NullablesWrapper<LocalDate>(null))

    private val endTimeSubject = BehaviorSubject.createDefault(NullablesWrapper<LocalTime>(null))

    private val endDateTimeSubject = endDateSubject.combineWithTimeAsSubject(endTimeSubject)

    private val previewImageSubject = BehaviorSubject.createDefault(NullablesWrapper<Uri>(null))

    private val isStreamCreationAvailableObservable = Observable
        .combineLatestArray<NullablesWrapper<*>, Boolean>(
            arrayOf(
                titleSubject,
                descriptionSubject,
                startDateSubject,
                startTimeSubject,
            )
        ) { requiredFields ->
            var isStreamCreationAvailable = true

            for (field in requiredFields) {
                if ((field as NullablesWrapper<*>).value == null) {
                    isStreamCreationAvailable = false
                    break
                }
            }

            isStreamCreationAvailable
        }
        .distinctUntilChanged()

    private val preferredVideoAspectRatio = AspectRatio(16, 9)

    private var streamCreationDisposable: Disposable? = null

    override val dataPreparationState: LiveData<ViewModelPreparationState> =
        MutableLiveData<ViewModelPreparationState>().apply {
            dataPreparationStateSubject
                .observeOn(mainThreadScheduler)
                .subscribeBy(
                    onNext = ::setValue,
                    onError = applicationErrorsLogger::logError
                )
                .addTo(disposables)
        }

    override val isAnyOperationInProgress: LiveData<Boolean> = MutableLiveData<Boolean>().apply {
        isAnyOperationInProgressObservable
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onNext = ::setValue,
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }

    override val nextDestinationEvent: LiveData<NextDestination> =
        LiveEvent<NextDestination>().apply {
            nextDestinationEventSubject
                .observeOn(mainThreadScheduler)
                .subscribeBy(
                    onNext = ::setValue,
                    onError = applicationErrorsLogger::logError
                )
                .addTo(disposables)
        }

    override val systemMessage: LiveData<String> = LiveEvent<String>().apply {
        systemMessageSubject
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onNext = ::setValue,
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }

    override val title: LiveData<String> = MutableLiveData<String>().apply {
        titleSubject
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onNext = { value = it.value ?: "" },
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }

    override val description: LiveData<String> = MutableLiveData<String>().apply {
        descriptionSubject
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onNext = { value = it.value ?: "" },
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }

    override val startDate: LiveData<LocalDate?> = MutableLiveData<LocalDate?>().apply {
        startDateSubject
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onNext = { value = it.value },
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }

    override val startTime: LiveData<LocalTime?> = MutableLiveData<LocalTime?>().apply {
        startTimeSubject
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onNext = { value = it.value },
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }

    override val startDateTime: LiveData<DateTime?> = MutableLiveData<DateTime?>().apply {
        startDateTimeSubject
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onNext = { value = it.value },
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }

    override val endDate: LiveData<LocalDate?> = MutableLiveData<LocalDate?>().apply {
        endDateSubject
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onNext = { value = it.value },
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }

    override val endTime: LiveData<LocalTime?> = MutableLiveData<LocalTime?>().apply {
        endTimeSubject
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onNext = { value = it.value },
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }

    override val endDateTime: LiveData<DateTime?> = MutableLiveData<DateTime?>().apply {
        endDateTimeSubject
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onNext = { value = it.value },
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }

    override val previewImage: LiveData<Uri?> = MutableLiveData<Uri?>().apply {
        previewImageSubject
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onNext = { value = it.value },
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }

    override val isStreamCreationAvailable: LiveData<Boolean> = MutableLiveData<Boolean>().apply {
        isStreamCreationAvailableObservable
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onNext = ::setValue,
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }

    override fun updateTitle(title: String) {
        launchLongTermOperation operation@{
            val currentTitle = titleSubject.value?.value
            val newTitle = if (title.isBlank()) null else title

            if (currentTitle == newTitle) return@operation

            titleSubject.onNext(NullablesWrapper(newTitle))
        }
    }

    override fun updateDescription(description: String) {
        launchLongTermOperation operation@{
            val currentDescription = descriptionSubject.value?.value
            val newDescription = if (description.isBlank()) null else description

            if (currentDescription == newDescription) return@operation

            descriptionSubject.onNext(NullablesWrapper(newDescription))
        }
    }

    override fun updateStartDate(dateInMillis: Long) {
        launchLongTermOperation operation@{
            val currentStartDate = startDateSubject.value?.value
            val newStartDate = LocalDate(dateInMillis)

            if (currentStartDate?.isEqual(newStartDate) == true) return@operation

            val endDate = endDateSubject.value?.value
            val endDateTime = endDateTimeSubject.value?.value
            val startTime = startTimeSubject.value?.value
            val newStartDateTime = startTime?.let(newStartDate::toDateTime)

            if (newStartDateTime != null) {
                if (endDateTime != null && newStartDateTime.isAfter(endDateTime)) {
                    sendSystemMessage(R.string.fragment_stream_creation_error_start_date_time_is_after_end_date_time)
                    return@operation
                }

                if (newStartDateTime.isBeforeNow) {
                    sendSystemMessage(R.string.fragment_stream_creation_error_start_date_time_is_before_now)
                    return@operation
                }
            }

            if (endDate?.isBefore(newStartDate) == true) {
                sendSystemMessage(R.string.fragment_stream_creation_error_start_date_is_after_end_date)
                return@operation
            }

            startDateSubject.onNext(NullablesWrapper(newStartDate))
        }
    }

    override fun updateStartTime(hour: Int, minute: Int) {
        launchLongTermOperation operation@{
            val adjustedHour = hour.coerceIn(0..23)
            val adjustedMinute = minute.coerceIn(0..59)

            val currentStartTime = startTimeSubject.value?.value
            val newStartTime = LocalTime(adjustedHour, adjustedMinute)

            if (currentStartTime?.isEqual(newStartTime) == true) return@operation

            val startDate = startDateSubject.value?.value
            val newStartDateTime = startDate?.toDateTime(newStartTime)
            val endDateTime = endDateTimeSubject.value?.value

            if (newStartDateTime != null) {
                if (endDateTime != null && newStartDateTime.isAfter(endDateTime)) {
                    sendSystemMessage(R.string.fragment_stream_creation_error_start_date_time_is_after_end_date_time)
                    return@operation
                }

                if (newStartDateTime.isBeforeNow) {
                    sendSystemMessage(R.string.fragment_stream_creation_error_start_date_time_is_before_now)
                    return@operation
                }
            }

            startTimeSubject.onNext(NullablesWrapper(newStartTime))
        }
    }

    override fun updateEndDate(dateInMillis: Long) {
        launchLongTermOperation operation@{
            val currentEndDate = endDateSubject.value?.value
            val newEndDate = LocalDate(dateInMillis)

            if (currentEndDate?.isEqual(newEndDate) == true) return@operation

            val startDate = startDateSubject.value?.value
            val startDateTime = startDateTimeSubject.value?.value
            val endTime = endTimeSubject.value?.value
            val newEndDateTime = endTime?.let(newEndDate::toDateTime)

            if (newEndDateTime != null) {
                if (startDateTime != null && newEndDateTime.isBefore(startDateTime)) {
                    sendSystemMessage(R.string.fragment_stream_creation_error_end_date_time_is_before_start_date_time)
                    return@operation
                }

                if (newEndDateTime.isBeforeNow) {
                    sendSystemMessage(R.string.fragment_stream_creation_error_end_date_time_is_before_now)
                    return@operation
                }
            }

            if (startDate?.isAfter(newEndDate) == true) {
                sendSystemMessage(R.string.fragment_stream_creation_error_end_date_is_before_start_date)
                return@operation
            }

            endDateSubject.onNext(NullablesWrapper(newEndDate))
        }
    }

    override fun updateEndTime(hour: Int, minute: Int) {
        launchLongTermOperation operation@{
            val adjustedHour = hour.coerceIn(0..23)
            val adjustedMinute = minute.coerceIn(0..59)

            val currentEndTime = endTimeSubject.value?.value
            val newEndTime = LocalTime(adjustedHour, adjustedMinute)

            if (currentEndTime?.isEqual(newEndTime) == true) return@operation

            val endDate = endDateSubject.value?.value
            val newEndDateTime = endDate?.toDateTime(newEndTime)
            val startDateTime = startDateTimeSubject.value?.value

            if (newEndDateTime != null) {
                if (startDateTime != null && newEndDateTime.isBefore(startDateTime)) {
                    sendSystemMessage(R.string.fragment_stream_creation_error_end_date_time_is_before_start_date_time)
                    return@operation
                }

                if (newEndDateTime.isBeforeNow) {
                    sendSystemMessage(R.string.fragment_stream_creation_error_end_date_time_is_before_now)
                    return@operation
                }
            }

            endTimeSubject.onNext(NullablesWrapper(newEndTime))
        }
    }

    override fun updatePreviewImage(uri: Uri?) {
        launchLongTermOperation operation@{
            val currentUri = previewImageSubject.value?.value

            if (currentUri == uri) return@operation

            previewImageSubject.onNext(NullablesWrapper(uri))
        }
    }

    override fun createStream() {
        incrementActiveOperationsCount()

        streamCreationDisposable?.dispose()

        val title = titleSubject.value?.value
        val description = descriptionSubject.value?.value
        val startDateTime = startDateTimeSubject.value?.value
        val endDateTime = endDateTimeSubject.value?.value

        if (title == null || description == null || startDateTime == null || endDateTime == null) {
            val errorMessage =
                applicationContext.getString(R.string.fragment_stream_creation_error_necessary_fields_are_not_filled_in)

            systemMessageSubject.onNext(errorMessage)
            decrementActiveOperationsCount()
            return
        }

        val defaultVideoResolution = getDefaultVideoResolution()

        val image = previewImageSubject.value?.value

        streamCreationDisposable = myStreamRepository
            .createMyStream(
                title,
                description,
                startDateTime,
                endDateTime,
                defaultVideoResolution.width,
                defaultVideoResolution.height,
                image
            )
            .observeOn(mainThreadScheduler)
            .doOnTerminate(::decrementActiveOperationsCount)
            .subscribeBy(
                onError = { exception ->
                    val errorMessage = applicationContext
                        .getString(R.string.fragment_stream_creation_error_failed_to_schedule_the_stream)

                    systemMessageSubject.onNext(errorMessage)

                    applicationErrorsLogger.logError(exception)
                }
            )
            .addTo(disposables)
    }

    private fun getDefaultVideoResolution(): Resolution {
        val horizontalResolution = cameraResolutionsProvider
            .getTheHighestResolutionOfBackCamera(preferredVideoAspectRatio)
            ?: cameraResolutionsProvider
                .getTheHighestResolutionOfFrontCamera(
                    preferredVideoAspectRatio
                )
            ?: Resolution(1280, 720)

        return horizontalResolution.rotate()
    }

    override fun prepareToCloseCurrentDestination() {
        launchLongTermOperation {
            nextDestinationEventSubject.onNext(NextDestination.Close)
        }
    }

    override fun prepareToNavigateToStartDatePickerDestination() {
        launchLongTermOperation {
            val endDateTime = endDateTimeSubject.value?.value
            val endDate = endDateSubject.value?.value

            val minDate = DateTime().withTimeAtStartOfDay().millis
            val maxDate = endDateTime?.millis ?: endDate?.toDateTimeAtStartOfDay()?.millis

            nextDestinationEventSubject.onNext(
                NextDestination.StartDatePicker(minDate, maxDate)
            )
        }
    }

    override fun prepareToNavigateToStartTimePickerDestination() {
        launchLongTermOperation {
            nextDestinationEventSubject.onNext(NextDestination.StartTimePicker)
        }
    }

    override fun prepareToNavigateToEndDatePickerDestination() {
        launchLongTermOperation operation@{
            val startDateTime = startDateTimeSubject.value?.value
            val startDate = startDateSubject.value?.value

            val minDate = startDateTime?.millis
                ?: startDate?.toDateTimeAtStartOfDay()?.millis
                ?: LocalDate().toDateTimeAtStartOfDay().millis

            nextDestinationEventSubject.onNext(NextDestination.EndDatePicker(minDate))
        }
    }

    override fun prepareToNavigateToEndTimePickerDestination() {
        launchLongTermOperation {
            nextDestinationEventSubject.onNext(NextDestination.EndTimePicker)
        }
    }

    override fun prepareToNavigateToPreviewImagePickerDestination() {
        launchLongTermOperation {
            nextDestinationEventSubject.onNext(NextDestination.PreviewImagePicker)
        }
    }

    private fun ObservableSource<NullablesWrapper<LocalDate>>.combineWithTimeAsSubject(
        timeSource: ObservableSource<NullablesWrapper<LocalTime>>
    ) = BehaviorSubject.createDefault(NullablesWrapper<DateTime>(null)).apply {
        Observable
            .combineLatest(
                this@combineWithTimeAsSubject,
                timeSource,
                BiFunction<NullablesWrapper<LocalDate>, NullablesWrapper<LocalTime>, NullablesWrapper<DateTime>> { nullableDate, nullableTime ->
                    val date =
                        nullableDate.value ?: return@BiFunction NullablesWrapper(null)

                    val time =
                        nullableTime.value ?: return@BiFunction NullablesWrapper(null)

                    val dateTime = date.toDateTime(time)

                    NullablesWrapper(dateTime)
                })
            .subscribeBy(this::onError, this::onComplete, this::onNext)
            .addTo(disposables)
    }

    private fun sendSystemMessage(@StringRes resourceId: Int) {
        val message = applicationContext.getString(resourceId)
        systemMessageSubject.onNext(message)
    }
}