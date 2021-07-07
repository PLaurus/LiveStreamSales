package tv.wfc.livestreamsales.features.profile.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding4.view.clicks
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import tv.wfc.livestreamsales.R
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.ComputationScheduler
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.MainThreadScheduler
import tv.wfc.livestreamsales.application.tools.errors.IApplicationErrorsLogger
import tv.wfc.livestreamsales.application.ui.base.BaseFragment
import tv.wfc.livestreamsales.databinding.FragmentProfileBinding
import tv.wfc.livestreamsales.features.home.ui.HomeFragment
import tv.wfc.livestreamsales.features.home.ui.HomeFragmentDirections
import tv.wfc.livestreamsales.features.profile.di.ProfileComponent
import tv.wfc.livestreamsales.features.profile.viewmodel.IProfileViewModel
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ProfileFragment: BaseFragment(R.layout.fragment_profile) {
    private val navigationController by lazy { findNavController() }

    private var viewBinding: FragmentProfileBinding? = null

    private lateinit var profileComponent: ProfileComponent

    @Inject
    lateinit var viewModel: IProfileViewModel

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
        createProfileComponent()
        injectDependencies()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindView(view)
        initializeContentLoader()
        manageNavigation()
    }

    override fun onDestroyView() {
        unbindView()
        super.onDestroyView()
    }

    private fun createProfileComponent(){
        profileComponent = appComponent
            .profileComponent()
            .create(this)
    }

    private fun injectDependencies(){
        profileComponent.inject(this)
    }

    private fun bindView(view: View){
        viewBinding = FragmentProfileBinding.bind(view)
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
        initializeUpdateUserPersonalInformationButton()
        initializeChangePaymentInformationButton()
        initializeLogOutButton()
        initializeSnackBar()
    }

    private fun manageNavigation(){
        viewModel.isUserAuthorized.observe(viewLifecycleOwner, { isUserAuthorized ->
            if(!isUserAuthorized) navigateToMainPage()
        })
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
                is IProfileViewModel.NameError.FieldIsRequired -> getString(R.string.fragment_profile_field_is_required)
                is IProfileViewModel.NameError.FieldContainsIllegalSymbols -> getString(R.string.fragment_profile_field_contains_illegal_symbols)
                is IProfileViewModel.NameError.StartsWithWhitespace -> getString(R.string.fragment_profile_field_starts_with_whitespace)
                is IProfileViewModel.NameError.EndsWithWhitespace -> getString(R.string.fragment_profile_field_ends_with_whitespace)
                is IProfileViewModel.NameError.RepetitiveWhitespaces -> getString(R.string.fragment_profile_field_repetitive_whitespaces)
                is IProfileViewModel.NameError.LengthIsTooShort -> getString(R.string.fragment_profile_field_length_is_too_short, nameError.minLength)
                is IProfileViewModel.NameError.LengthIsTooLong -> getString(R.string.fragment_profile_field_length_is_too_long, nameError.maxLength)
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
                is IProfileViewModel.SurnameError.FieldContainsIllegalSymbols -> getString(R.string.fragment_profile_field_contains_illegal_symbols)
                is IProfileViewModel.SurnameError.StartsWithWhitespace -> getString(R.string.fragment_profile_field_starts_with_whitespace)
                is IProfileViewModel.SurnameError.EndsWithWhitespace -> getString(R.string.fragment_profile_field_ends_with_whitespace)
                is IProfileViewModel.SurnameError.RepetitiveWhitespaces -> getString(R.string.fragment_profile_field_repetitive_whitespaces)
                is IProfileViewModel.SurnameError.LengthIsTooShort -> getString(R.string.fragment_profile_field_length_is_too_short, surnameError.minLength)
                is IProfileViewModel.SurnameError.LengthIsTooLong -> getString(R.string.fragment_profile_field_length_is_too_long, surnameError.maxLength)
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
                is IProfileViewModel.EmailError.FieldIsRequired -> getString(R.string.fragment_profile_field_is_required)
                is IProfileViewModel.EmailError.IllegalEmailFormat -> getString(R.string.fragment_profile_email_has_wrong_format)
                is IProfileViewModel.EmailError.LengthIsTooShort -> getString(R.string.fragment_profile_field_length_is_too_short, emailError.minLength)
                is IProfileViewModel.EmailError.LengthIsTooLong -> getString(R.string.fragment_profile_field_length_is_too_long, emailError.maxLength)
                else -> null
            }

            viewBinding?.emailLayout?.run{
                isErrorEnabled = errorMessage != null
                error = errorMessage
            }
        })
    }

    private fun initializeUpdateUserPersonalInformationButton(){
        viewBinding?.updateUserPersonalInformationButton?.run{
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

    private fun initializeChangePaymentInformationButton(){
        viewBinding?.changePaymentInformationButton?.run{
            clicks()
                .throttleLatest(500L, TimeUnit.MILLISECONDS, computationScheduler)
                .observeOn(mainThreadScheduler)
                .subscribeBy(
                    onNext = { navigateToPaymentCardInformation() },
                    onError = applicationErrorsLogger::logError
                )
                .addTo(viewScopeDisposables)
        }
    }

    private fun initializeLogOutButton(){
        viewBinding?.logOutButton?.run{
            clicks()
                .throttleLatest(500L, TimeUnit.MILLISECONDS, computationScheduler)
                .observeOn(mainThreadScheduler)
                .subscribeBy(
                    onNext = { viewModel.logOut() },
                    onError = applicationErrorsLogger::logError
                )
                .addTo(viewScopeDisposables)
        }
    }

    private fun initializeSnackBar(){
        val rootView = viewBinding?.root ?: return

        viewModel.isUserPersonalInformationSaved.observe(viewLifecycleOwner){
            Snackbar
                .make(
                    rootView,
                    R.string.fragment_profile_user_personal_information_is_saved,
                    Snackbar.LENGTH_LONG
                )
                .show()
        }
    }

    private fun navigateToMainPage(){
        (parentFragment as HomeFragment).navigateToMainPage()
    }

    private fun navigateToPaymentCardInformation(){
        val action = HomeFragmentDirections.actionToPaymentCardInformationDestination()
        navigationController.navigate(action)
    }
}