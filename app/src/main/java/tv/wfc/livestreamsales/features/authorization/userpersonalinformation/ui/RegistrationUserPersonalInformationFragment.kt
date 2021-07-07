package tv.wfc.livestreamsales.features.authorization.userpersonalinformation.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.addCallback
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.jakewharton.rxbinding4.view.clicks
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import tv.wfc.livestreamsales.R
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.ComputationScheduler
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.MainThreadScheduler
import tv.wfc.livestreamsales.application.tools.errors.IApplicationErrorsLogger
import tv.wfc.livestreamsales.application.ui.base.BaseFragment
import tv.wfc.livestreamsales.databinding.FragmentRegistrationUserPersonalInformationBinding
import tv.wfc.livestreamsales.features.authorization.userpersonalinformation.di.RegistrationUserPersonalInformationComponent
import tv.wfc.livestreamsales.features.authorization.userpersonalinformation.viewmodel.IRegistrationUserPersonalInformationViewModel
import tv.wfc.livestreamsales.features.mainappcontent.ui.MainAppContentActivity
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class RegistrationUserPersonalInformationFragment: BaseFragment(R.layout.fragment_registration_user_personal_information) {
    private val navigationController by lazy { findNavController() }
    private val navigationArguments by navArgs<RegistrationUserPersonalInformationFragmentArgs>()

    private val onToolbarBackPressed = object: MainAppContentActivity.ToolbarNavigationOnClickListener{
        override fun onClick() {
            (requireActivity() as MainAppContentActivity).removeToolbarNavigationOnClickListener(this)
            viewModel.logOut()
        }
    }

    private var viewBinding: FragmentRegistrationUserPersonalInformationBinding? = null

    private lateinit var registrationUserPersonalInformationComponent: RegistrationUserPersonalInformationComponent

    @Inject
    lateinit var viewModel: IRegistrationUserPersonalInformationViewModel

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
        createRegistrationUserPersonalInformationComponent()
        injectDependencies()
        setIsViewModelInRegistrationMode()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindActivityToolbar()
        bindView(view)
        initializeContentLoader()
        manageNavigation()
    }

    override fun onDestroyView() {
        unbindActivityToolbar()
        unbindView()
        super.onDestroyView()
    }

    private fun createRegistrationUserPersonalInformationComponent(){
        val authorizationComponent = appComponent
            .authorizationComponent()
            .create()

        registrationUserPersonalInformationComponent = authorizationComponent
            .registrationUserPersonalInformationComponent()
            .create(this)
    }

    private fun injectDependencies(){
        registrationUserPersonalInformationComponent.inject(this)
    }

    private fun setIsViewModelInRegistrationMode(){
        if(navigationArguments.isRegistrationFlow){
            val authorizationToken = navigationArguments.token
            if(authorizationToken != null){
                viewModel.setViewModelToRegistrationFlow(authorizationToken)
            }
        }
    }

    private fun bindActivityToolbar(){
        if(!navigationArguments.isRegistrationFlow) return
        (requireActivity() as MainAppContentActivity).addToolbarNavigationOnClickListener(onToolbarBackPressed)
    }

    private fun unbindActivityToolbar(){
        if(!navigationArguments.isRegistrationFlow) return
        (requireActivity() as MainAppContentActivity).removeToolbarNavigationOnClickListener(onToolbarBackPressed)
    }

    private fun bindView(view: View){
        viewBinding = FragmentRegistrationUserPersonalInformationBinding.bind(view)
    }

    private fun unbindView(){
        viewBinding = null
    }

    private fun initializeContentLoader(){
        viewBinding?.contentLoader?.apply {
            clearPreparationListeners()
            attachViewModel(viewLifecycleOwner, viewModel)
            addOnDataIsPreparedListener(::onDataIsPrepared)

            viewModel.isAnyOperationInProgress.observe(viewLifecycleOwner){ isAnyOperationInProgress ->
                if(isAnyOperationInProgress){
                    showOperationProgress()
                } else {
                    hideOperationProgress()
                }
            }
        }
    }

    private fun onDataIsPrepared() {
        initializeNameEditText()
        initializeNameLayout()
        initializeSurnameEditText()
        initializeSurnameLayout()
        initializePhoneNumberEditText()
        initializeEmailEditText()
        initializeEmailLayout()
        initializeContinueButton()
    }

    private fun manageNavigation(){
        viewModel.isUserAuthorized.observe(viewLifecycleOwner, { isUserAuthorized ->
            if(!isUserAuthorized) navigationController.navigateUp()
        })

        viewModel.isUserPersonalInformationSaved.observe(viewLifecycleOwner){
            navigateToPaymentCardInformation()
        }

        if(navigationArguments.isRegistrationFlow){
            requireActivity().onBackPressedDispatcher.addCallback(this){
                viewModel.logOut()
            }
        }
    }

    private fun initializeNameEditText(){
        viewBinding?.nameEditText?.run {
            viewModel.name.observe(viewLifecycleOwner, Observer{ name ->
                if(text.toString() == name) return@Observer

                setText(name, TextView.BufferType.EDITABLE)
            })

            addTextChangedListener { editable ->
                viewModel.updateName(editable.toString())
            }
        }
    }

    private fun initializeNameLayout(){
        viewModel.nameError.observe(viewLifecycleOwner, { nameError ->
            val errorMessage = when(nameError){
                is IRegistrationUserPersonalInformationViewModel.NameError.FieldIsRequired -> getString(R.string.fragment_registration_user_personal_information_field_is_required)
                is IRegistrationUserPersonalInformationViewModel.NameError.FieldContainsIllegalSymbols -> getString(R.string.fragment_registration_user_personal_information_field_contains_illegal_symbols)
                is IRegistrationUserPersonalInformationViewModel.NameError.StartsWithWhitespace -> getString(R.string.fragment_registration_user_personal_information_field_starts_with_whitespace)
                is IRegistrationUserPersonalInformationViewModel.NameError.EndsWithWhitespace -> getString(R.string.fragment_registration_user_personal_information_field_ends_with_whitespace)
                is IRegistrationUserPersonalInformationViewModel.NameError.RepetitiveWhitespaces -> getString(R.string.fragment_registration_user_personal_information_field_repetitive_whitespaces)
                is IRegistrationUserPersonalInformationViewModel.NameError.LengthIsTooShort -> getString(R.string.fragment_registration_user_personal_information_field_length_is_too_short, nameError.minLength)
                is IRegistrationUserPersonalInformationViewModel.NameError.LengthIsTooLong -> getString(R.string.fragment_registration_user_personal_information_field_length_is_too_long, nameError.maxLength)
                else -> null
            }

            viewBinding?.nameLayout?.run{
                isErrorEnabled = errorMessage != null
                error = errorMessage
            }
        })
    }

    private fun initializeSurnameEditText(){
        viewBinding?.surnameEditText?.run {
            viewModel.surname.observe(viewLifecycleOwner, Observer{ surname ->
                if(text.toString() == surname) return@Observer

                setText(surname, TextView.BufferType.EDITABLE)
            })

            addTextChangedListener { editable ->
                viewModel.updateSurname(editable.toString())
            }
        }
    }

    private fun initializeSurnameLayout(){
        viewModel.surnameError.observe(viewLifecycleOwner, { surnameError ->
            val errorMessage = when(surnameError){
                is IRegistrationUserPersonalInformationViewModel.SurnameError.FieldContainsIllegalSymbols -> getString(R.string.fragment_registration_user_personal_information_field_contains_illegal_symbols)
                is IRegistrationUserPersonalInformationViewModel.SurnameError.StartsWithWhitespace -> getString(R.string.fragment_registration_user_personal_information_field_starts_with_whitespace)
                is IRegistrationUserPersonalInformationViewModel.SurnameError.EndsWithWhitespace -> getString(R.string.fragment_registration_user_personal_information_field_ends_with_whitespace)
                is IRegistrationUserPersonalInformationViewModel.SurnameError.RepetitiveWhitespaces -> getString(R.string.fragment_registration_user_personal_information_field_repetitive_whitespaces)
                is IRegistrationUserPersonalInformationViewModel.SurnameError.LengthIsTooShort -> getString(R.string.fragment_registration_user_personal_information_field_length_is_too_short, surnameError.minLength)
                is IRegistrationUserPersonalInformationViewModel.SurnameError.LengthIsTooLong -> getString(R.string.fragment_registration_user_personal_information_field_length_is_too_long, surnameError.maxLength)
                else -> null
            }

            viewBinding?.surnameLayout?.run{
                isErrorEnabled = errorMessage != null
                error = errorMessage
            }
        })
    }

    private fun initializePhoneNumberEditText(){
        viewBinding?.phoneNumberEditText?.run {
            viewModel.phoneNumber.observe(viewLifecycleOwner, Observer{ phoneNumber ->
                if(text.toString() == phoneNumber) return@Observer

                setText(phoneNumber, TextView.BufferType.EDITABLE)
            })
        }
    }

    private fun initializeEmailEditText(){
        viewBinding?.emailEditText?.run {
            viewModel.email.observe(viewLifecycleOwner, Observer{ email ->
                if(text.toString() == email) return@Observer

                setText(email, TextView.BufferType.EDITABLE)
            })

            addTextChangedListener { editable ->
                viewModel.updateEmail(editable.toString())
            }
        }
    }

    private fun initializeEmailLayout(){
        viewModel.emailError.observe(viewLifecycleOwner, { emailError ->
            val errorMessage = when(emailError){
                is IRegistrationUserPersonalInformationViewModel.EmailError.FieldIsRequired -> getString(R.string.fragment_registration_user_personal_information_field_is_required)
                is IRegistrationUserPersonalInformationViewModel.EmailError.IllegalEmailFormat -> getString(R.string.fragment_registration_user_personal_information_email_has_wrong_format)
                is IRegistrationUserPersonalInformationViewModel.EmailError.LengthIsTooShort -> getString(R.string.fragment_registration_user_personal_information_field_length_is_too_short, emailError.minLength)
                is IRegistrationUserPersonalInformationViewModel.EmailError.LengthIsTooLong -> getString(R.string.fragment_registration_user_personal_information_field_length_is_too_long, emailError.maxLength)
                else -> null
            }

            viewBinding?.emailLayout?.run{
                isErrorEnabled = errorMessage != null
                error = errorMessage
            }
        })
    }

    private fun initializeContinueButton(){
        viewBinding?.continueButton?.run{
            viewModel.isCurrentUserPersonalInformationCorrect.observe(viewLifecycleOwner){ isUserPersonalInformationCorrect ->
                isEnabled = isUserPersonalInformationCorrect
            }

            clicks()
                .throttleLatest(500, TimeUnit.MILLISECONDS, computationScheduler)
                .observeOn(mainThreadScheduler)
                .subscribeBy(
                    onNext = { viewModel.saveUserPersonalInformation() },
                    onError = applicationErrorsLogger::logError
                )
                .addTo(viewScopeDisposables)
        }
    }

    private fun navigateToPaymentCardInformation(){
        val action = RegistrationUserPersonalInformationFragmentDirections.actionRegistrationUserPersonalInformationDestinationToRegistrationPaymentCardInformationDestination()
        navigationController.navigate(action)
    }
}