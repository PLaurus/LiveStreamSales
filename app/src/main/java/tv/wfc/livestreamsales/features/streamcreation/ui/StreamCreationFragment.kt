package tv.wfc.livestreamsales.features.streamcreation.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.format.DateFormat.is24HourFormat
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import coil.ImageLoader
import coil.request.Disposable
import coil.request.ImageRequest
import com.google.android.material.datepicker.*
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.jakewharton.rxbinding4.view.clicks
import com.laurus.p.tools.context.getDrawableCompat
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import tv.wfc.livestreamsales.R
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.ComputationScheduler
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.MainThreadScheduler
import tv.wfc.livestreamsales.application.tools.errors.IApplicationErrorsLogger
import tv.wfc.livestreamsales.application.ui.base.BaseFragment
import tv.wfc.livestreamsales.databinding.FragmentStreamCreationBinding
import tv.wfc.livestreamsales.features.mainappcontent.ui.MainAppContentActivity
import tv.wfc.livestreamsales.features.streamcreation.di.StreamCreationComponent
import tv.wfc.livestreamsales.features.streamcreation.model.NextDestination
import tv.wfc.livestreamsales.features.streamcreation.viewmodel.IStreamCreationViewModel
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class StreamCreationFragment : BaseFragment(R.layout.fragment_stream_creation) {
    private companion object {
        private const val REQUEST_CODE_PICK_PREVIEW_IMAGE = 0
    }

    private val navigationController by lazy { findNavController() }

    private val onToolbarBackPressed =
        object : MainAppContentActivity.ToolbarNavigationOnClickListener {
            override fun onClick() {
                (requireActivity() as MainAppContentActivity)
                    .removeToolbarNavigationOnClickListener(this)

                viewModel.prepareToCloseCurrentDestination()
            }
        }

    private lateinit var dependenciesComponent: StreamCreationComponent

    private var viewBinding: FragmentStreamCreationBinding? = null

    private var previewImageLoaderDisposable: Disposable? = null

    @Inject
    lateinit var viewModel: IStreamCreationViewModel

    @Inject
    lateinit var imageLoader: ImageLoader

    @Inject
    @MainThreadScheduler
    lateinit var mainThreadScheduler: Scheduler

    @Inject
    @ComputationScheduler
    lateinit var computationScheduler: Scheduler

    @Inject
    lateinit var applicationErrorsLogger: IApplicationErrorsLogger

    override fun onAttach(context: Context) {
        super.onAttach(context)
        initializeDependenciesComponent()
        injectDependencies()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindActivityToolbar()
        bindView(view)
        manageNavigation()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQUEST_CODE_PICK_PREVIEW_IMAGE -> {
                if (resultCode == Activity.RESULT_OK) {
                    data?.data?.also(viewModel::updatePreviewImage)
                }
            }
        }
    }

    override fun onDestroyView() {
        unbindActivityToolbar()
        unbindView()
        super.onDestroyView()
    }

    private fun initializeDependenciesComponent() {
        if (::dependenciesComponent.isInitialized) return

        dependenciesComponent = appComponent
            .broadcastCreationComponentFactory()
            .create(this)
    }

    private fun injectDependencies() {
        dependenciesComponent.inject(this)
    }

    private fun bindView(view: View) {
        viewBinding = FragmentStreamCreationBinding.bind(view)
        viewBinding?.initialize()
    }

    private fun unbindView() {
        viewBinding = null
    }

    private fun manageNavigation() {
        viewModel.nextDestinationEvent.observe(viewLifecycleOwner) { nextDestination ->
            when (nextDestination) {
                NextDestination.Close -> navigationController.navigateUp()
                is NextDestination.StartDatePicker -> {
                    navigateToStartDatePicker(nextDestination.minDate, nextDestination.maxDate)
                }
                NextDestination.StartTimePicker -> navigateToStartTimePicker()
                is NextDestination.EndDatePicker -> navigateToEndDatePicker(nextDestination.minDate)
                NextDestination.EndTimePicker -> navigateToEndTimePicker()
                NextDestination.PreviewImagePicker -> navigateToPreviewImagePicker()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            viewModel.prepareToCloseCurrentDestination()
        }
    }

    private fun bindActivityToolbar() {
        (requireActivity() as MainAppContentActivity)
            .addToolbarNavigationOnClickListener(onToolbarBackPressed)
    }

    private fun unbindActivityToolbar() {
        (requireActivity() as MainAppContentActivity)
            .removeToolbarNavigationOnClickListener(onToolbarBackPressed)
    }

    private fun FragmentStreamCreationBinding.initialize() {
        initializeContentLoader()
        initializeNameTextInputEditText()
        initializeDescriptionTextInputEditText()
        initializeStartDateButton()
        initializeStartTimeButton()
        initializeEndDateButton()
        initializeEndTimeButton()
        initializeEndDateTimeGroup()
        initializePreviewImage()
        initializePreviewCard()
        initializeCreateButton()
        initializeToast()
    }

    private fun FragmentStreamCreationBinding.initializeContentLoader() {
        contentLoader.apply {
            clearPreparationListeners()
            attachViewModel(viewLifecycleOwner, viewModel)

            viewModel.isAnyOperationInProgress.observe(viewLifecycleOwner) { isAnyOperationInProgress ->
                when (isAnyOperationInProgress) {
                    true -> showOperationProgress()
                    false -> hideOperationProgress()
                }
            }
        }
    }

    private fun FragmentStreamCreationBinding.initializeNameTextInputEditText() {
        nameTextInputEditText.apply {
            viewModel.title.observe(viewLifecycleOwner) { newTitle ->
                text?.apply { replace(0, length, newTitle) }
            }

            addTextChangedListener {
                viewModel.updateTitle(it?.toString() ?: "")
            }
        }
    }

    private fun FragmentStreamCreationBinding.initializeDescriptionTextInputEditText() {
        descriptionTextInputEditText.apply {
            viewModel.description.observe(viewLifecycleOwner) { newDescription ->
                text?.apply { replace(0, length, newDescription) }
            }

            addTextChangedListener {
                viewModel.updateDescription(it?.toString() ?: "")
            }
        }
    }

    private fun FragmentStreamCreationBinding.initializeStartDateButton() {
        startDateButton.apply {
            val startDateIsNotSetText =
                getString(R.string.fragment_stream_creation_start_date_not_set)

            viewModel.startDate.observe(viewLifecycleOwner) { startDate ->
                valueText = startDate?.toString("dd.MM.YYYY") ?: startDateIsNotSetText
            }

            clicks()
                .throttleLatest(500L, TimeUnit.MILLISECONDS, computationScheduler)
                .observeOn(mainThreadScheduler)
                .subscribeBy(
                    onNext = {
                        viewModel.prepareToNavigateToStartDatePickerDestination()
                    },
                    onError = applicationErrorsLogger::logError
                )
                .addTo(viewScopeDisposables)
        }
    }

    private fun FragmentStreamCreationBinding.initializeStartTimeButton() {
        startTimeButton.apply {
            val startTimeIsNotSetText =
                getString(R.string.fragment_stream_creation_start_time_not_set)

            viewModel.startTime.observe(viewLifecycleOwner) { startTime ->
                valueText = startTime?.toString("HH:mm") ?: startTimeIsNotSetText
            }

            clicks()
                .throttleLatest(500L, TimeUnit.MILLISECONDS, computationScheduler)
                .observeOn(mainThreadScheduler)
                .subscribeBy(
                    onNext = {
                        viewModel.prepareToNavigateToStartTimePickerDestination()
                    },
                    onError = applicationErrorsLogger::logError
                )
                .addTo(viewScopeDisposables)
        }
    }

    private fun FragmentStreamCreationBinding.initializeEndDateButton() {
        endDateButton.apply {
            val endDateIsNotSetText =
                context.getString(R.string.fragment_stream_creation_end_date_not_set)

            viewModel.endDate.observe(viewLifecycleOwner) { endDate ->
                valueText = endDate?.toString("dd:MM:YYYY") ?: endDateIsNotSetText
            }

            clicks()
                .throttleLatest(500L, TimeUnit.MILLISECONDS, computationScheduler)
                .observeOn(mainThreadScheduler)
                .subscribeBy(
                    onNext = {
                        viewModel.prepareToNavigateToEndDatePickerDestination()
                    },
                    onError = applicationErrorsLogger::logError
                )
                .addTo(viewScopeDisposables)
        }
    }

    private fun FragmentStreamCreationBinding.initializeEndTimeButton() {
        endTimeButton.apply {
            val endTimeIsNotSetText = getString(R.string.fragment_stream_creation_end_time_not_set)

            viewModel.endTime.observe(viewLifecycleOwner) { endTime ->
                valueText = endTime?.toString("HH:mm") ?: endTimeIsNotSetText
            }

            clicks()
                .throttleLatest(500L, TimeUnit.MILLISECONDS, computationScheduler)
                .observeOn(mainThreadScheduler)
                .subscribeBy(
                    onNext = {
                        viewModel.prepareToNavigateToEndTimePickerDestination()
                    },
                    onError = applicationErrorsLogger::logError
                )
                .addTo(viewScopeDisposables)
        }
    }

    private fun FragmentStreamCreationBinding.initializeEndDateTimeGroup() {
        endDateTimeGroup.apply {
            viewModel.startDateTime.observe(viewLifecycleOwner) { startDateTime ->
                visibility = if(startDateTime == null) View.GONE else View.VISIBLE
            }
        }
    }

    private fun FragmentStreamCreationBinding.initializePreviewImage() {
        val placeholder = requireContext().getDrawableCompat(
            R.drawable.ic_baseline_image_24,
            R.color.broadcastCreationFragment_previewImage_placeholder_tint
        )

        previewImage.apply {
            fun setPlaceholder() {
                setImageDrawable(placeholder)
                scaleType = ImageView.ScaleType.FIT_CENTER
            }

            fun setNewPreviewImage(image: Drawable) {
                setImageDrawable(image)
                scaleType = ImageView.ScaleType.CENTER_CROP
            }

            fun setNewPreviewImage(imageUri: Uri?) {
                val imageRequest = ImageRequest.Builder(context)
                    .data(imageUri)
                    .target(
                        onSuccess = ::setNewPreviewImage,
                        onError = { setPlaceholder() }
                    )
                    .build()

                previewImageLoaderDisposable?.dispose()
                previewImageLoaderDisposable = imageLoader.enqueue(imageRequest)

            }

            viewModel.previewImage.observe(viewLifecycleOwner) { imageUri ->
                if (imageUri == null) {
                    setPlaceholder()
                } else {
                    setNewPreviewImage(imageUri)
                }
            }
        }
    }

    private fun FragmentStreamCreationBinding.initializePreviewCard() {
        previewCard.apply {
            clicks()
                .throttleLatest(500L, TimeUnit.MILLISECONDS, computationScheduler)
                .observeOn(mainThreadScheduler)
                .subscribeBy(
                    onNext = {
                        viewModel.prepareToNavigateToPreviewImagePickerDestination()
                    },
                    onError = applicationErrorsLogger::logError
                )
                .addTo(viewScopeDisposables)
        }
    }

    private fun FragmentStreamCreationBinding.initializeCreateButton() {
        createButton.apply {
            viewModel.isStreamCreationAvailable.observe(viewLifecycleOwner, ::setEnabled)

            clicks()
                .throttleLatest(500L, TimeUnit.MILLISECONDS, computationScheduler)
                .observeOn(mainThreadScheduler)
                .subscribeBy(
                    onNext = { viewModel.createStream() },
                    onError = applicationErrorsLogger::logError
                )
                .addTo(viewScopeDisposables)
        }
    }

    private fun FragmentStreamCreationBinding.initializeToast(){
        viewModel.systemMessage.observe(viewLifecycleOwner) { systemMessage ->
            val toast = Toast.makeText(root.context, systemMessage, Toast.LENGTH_LONG)
            toast.show()
        }
    }

    private fun navigateToStartDatePicker(minDate: Long, maxDate: Long? = null) {
        val dateValidatorFrom = DateValidatorPointForward.from(minDate)
        val dateValidatorBefore = maxDate?.let(DateValidatorPointBackward::before)

        val dateValidators = listOfNotNull(dateValidatorFrom, dateValidatorBefore)

        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText(R.string.fragment_stream_creation_start_date_label)
            .setCalendarConstraints(
                CalendarConstraints.Builder()
                    .setValidator(CompositeDateValidator.allOf(dateValidators))
                    .build()
            )
            .build()

        datePicker.apply {
            addOnPositiveButtonClickListener(viewModel::updateStartDate)
            show(this@StreamCreationFragment.parentFragmentManager, "startDatePickerDestination")
        }
    }

    private fun navigateToStartTimePicker() {
        val timePicker = MaterialTimePicker.Builder()
            .setTimeFormat(if (is24HourFormat(requireContext())) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H)
            .setTitleText(R.string.fragment_stream_creation_start_time_label)
            .build()

        timePicker.apply {
            addOnPositiveButtonClickListener {
                val pickedHour = timePicker.hour
                val pickedMinute = timePicker.minute

                viewModel.updateStartTime(pickedHour, pickedMinute)
            }

            show(this@StreamCreationFragment.parentFragmentManager, "startTimePickerDestination")
        }
    }

    private fun navigateToEndDatePicker(minDate: Long) {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText(R.string.fragment_stream_creation_end_date_label)
            .setCalendarConstraints(
                CalendarConstraints.Builder()
                    .setValidator(DateValidatorPointForward.from(minDate))
                    .build()
            )
            .build()

        datePicker.apply {
            addOnPositiveButtonClickListener(viewModel::updateEndDate)
            show(this@StreamCreationFragment.parentFragmentManager, "endDatePickerDestination")
        }
    }

    private fun navigateToEndTimePicker() {
        val timePicker = MaterialTimePicker.Builder()
            .setTitleText(R.string.fragment_stream_creation_end_time_label)
            .setTimeFormat(if(is24HourFormat(requireContext())) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H)
            .build()

        timePicker.apply {
            addOnPositiveButtonClickListener {
                val pickedHour = timePicker.hour
                val pickedMinute = timePicker.minute

                viewModel.updateEndTime(pickedHour, pickedMinute)
            }

            show(this@StreamCreationFragment.parentFragmentManager, "endTimePickerDestination")
        }
    }

    private fun navigateToPreviewImagePicker() {
        val getIntent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
        }

        val pickIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        ).apply {
            type = "image/*"
        }

        val chooserTitle = requireContext().getString(R.string.fragment_stream_creation_choose_application_to_pick_image)
        val chooserIntent = Intent.createChooser(getIntent, chooserTitle).apply {
            putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(pickIntent))
        }

        startActivityForResult(chooserIntent, REQUEST_CODE_PICK_PREVIEW_IMAGE)
    }
}