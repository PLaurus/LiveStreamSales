package tv.wfc.livestreamsales.features.usersettings.ui

import android.content.Context
import android.os.Bundle
import android.text.InputFilter
import android.text.Selection
import android.view.View
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import com.jakewharton.rxbinding4.view.clicks
import com.laurus.p.edittextformatters.TextDividedOnPartsFormatter
import com.laurus.p.tools.edittext.addFilter
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import tv.wfc.livestreamsales.R
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.ComputationScheduler
import tv.wfc.livestreamsales.application.tools.errors.IApplicationErrorsLogger
import tv.wfc.livestreamsales.databinding.FragmentSettingsBinding
import tv.wfc.livestreamsales.features.authorizeduser.ui.base.AuthorizedUserFragment
import tv.wfc.livestreamsales.features.usersettings.di.UserSettingsComponent
import tv.wfc.livestreamsales.features.usersettings.viewmodel.IUserSettingsViewModel
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class UserSettingsFragment: AuthorizedUserFragment(R.layout.fragment_settings) {
    private val cardNumberMaxLength by lazy {
        val digitsCount = resources.getInteger(R.integer.paymentCardEditor_numberEditText_length)
        val spacesCount = 3
        digitsCount + spacesCount
    }

    private val paymentCardNumberTextFormatter by lazy{
        TextDividedOnPartsFormatter(
            4,
            ' ',
            viewModel::updateCardNumber
        )
    }

    private var viewBinding: FragmentSettingsBinding? = null

    private lateinit var userSettingsComponent: UserSettingsComponent

    @Inject
    lateinit var viewModel: IUserSettingsViewModel

    @Inject
    @ComputationScheduler
    lateinit var computationScheduler: Scheduler

    @Inject
    lateinit var applicationErrorsLogger: IApplicationErrorsLogger

    override fun onAttach(context: Context) {
        super.onAttach(context)
        initializeUserSettingsComponent()
        injectDependencies()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindView(view)
        initializeContentLoader()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unbindView()
    }

    private fun initializeUserSettingsComponent(){
        userSettingsComponent = authorizedUserComponent.settingsComponent().create(this)
    }

    private fun injectDependencies(){
        userSettingsComponent.inject(this)
    }

    private fun bindView(view: View){
        viewBinding = FragmentSettingsBinding.bind(view)
    }

    private fun unbindView(){
        viewBinding = null
    }

    private fun initializeContentLoader(){
        viewBinding?.contentLoader?.apply {
            clearPreparationListeners()
            attachViewModel(viewLifecycleOwner, viewModel)
            addOnDataIsPreparedListener(::onDataIsPrepared)
        }
    }

    private fun onDataIsPrepared() {
        initializeOperationProgressView()
        initializeNameEditText()
        initializeSurnameNameEditText()
        initializePhoneNumberEditText()
        initializeEmailEditText()
        initializeSaveUserPersonalInformationButton()
        initializeCardNumberEditText()
    }

    private fun initializeOperationProgressView(){
        val contentLoader = viewBinding?.contentLoader

        viewModel.isProcessingData.observe(viewLifecycleOwner, { dataSavingState ->
            if(dataSavingState == true){
                contentLoader?.showOperationProgress()
            } else{
                contentLoader?.hideOperationProgress()
            }
        })
    }

    private fun initializeNameEditText(){
        viewBinding?.userPersonalInformationLayout?.nameEditText?.run {
            viewModel.name.observe(viewLifecycleOwner, Observer{ name ->
                if(text.toString() == name) return@Observer

                setText(name, TextView.BufferType.EDITABLE)
            })

            addTextChangedListener { editable ->
                viewModel.updateName(editable.toString())
            }
        }
    }

    private fun initializeSurnameNameEditText(){
        viewBinding?.userPersonalInformationLayout?.surnameEditText?.run {
            viewModel.surname.observe(viewLifecycleOwner, Observer{ surname ->
                if(text.toString() == surname) return@Observer

                setText(surname, TextView.BufferType.EDITABLE)
            })

            addTextChangedListener { editable ->
                viewModel.updateSurname(editable.toString())
            }
        }
    }

    private fun initializePhoneNumberEditText(){
        viewBinding?.userPersonalInformationLayout?.phoneNumberEditText?.run {
            viewModel.phoneNumber.observe(viewLifecycleOwner, Observer{ phoneNumber ->
                if(text.toString() == phoneNumber) return@Observer

                setText(phoneNumber, TextView.BufferType.EDITABLE)
            })
        }
    }

    private fun initializeEmailEditText(){
        viewBinding?.userPersonalInformationLayout?.emailEditText?.run {
            viewModel.email.observe(viewLifecycleOwner, Observer{ email ->
                if(text.toString() == email) return@Observer

                setText(email, TextView.BufferType.EDITABLE)
            })

            addTextChangedListener { editable ->
                viewModel.updateEmail(editable.toString())
            }
        }
    }

    private fun initializeSaveUserPersonalInformationButton(){
        viewBinding?.userPersonalInformationLayout?.saveButton?.run {
            clicks()
                .throttleFirst(1L, TimeUnit.SECONDS, computationScheduler)
                .subscribeBy(
                    onNext = { viewModel.saveUserPersonalData() },
                    onError = applicationErrorsLogger::logError
                )
                .addTo(viewScopeDisposables)
        }
    }

    private fun initializeCardNumberEditText(){
        viewBinding?.paymentCardEditorLayout?.cardNumberEditText?.apply {
            addFilter(InputFilter.LengthFilter(cardNumberMaxLength))
            addTextChangedListener(paymentCardNumberTextFormatter)

            viewModel.cardNumber.observe(viewLifecycleOwner, Observer{ cardNumber ->
                val formattedCardNumber = paymentCardNumberTextFormatter.format(cardNumber)

                if(formattedCardNumber.toString() == text.toString()){
                    return@Observer
                }

                setText(cardNumber, TextView.BufferType.EDITABLE)
                Selection.setSelection(text, text?.length ?: 0)
            })
        }
    }
}