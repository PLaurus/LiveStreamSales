package com.example.livestreamsales.ui.fragment.telephonenumberinput

import android.content.Context
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.text.Editable
import android.text.Spannable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.example.livestreamsales.R
import com.example.livestreamsales.databinding.FragmentTelephoneNumberInputBinding
import com.example.livestreamsales.di.components.app.modules.reactivex.qualifiers.ComputationScheduler
import com.example.livestreamsales.di.components.app.modules.reactivex.qualifiers.MainThreadScheduler
import com.example.livestreamsales.di.components.phoneinput.PhoneInputComponent
import com.example.livestreamsales.ui.fragment.base.AuthorizationFragment
import com.example.livestreamsales.viewmodels.authorization.IAuthorizationViewModel
import com.example.livestreamsales.viewmodels.phoneinput.IPhoneInputViewModel
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding4.view.clicks
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.kotlin.addTo
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class TelephoneNumberInputFragment: AuthorizationFragment(R.layout.fragment_telephone_number_input) {
    private val codeRequestErrorSnackbar: Snackbar? by lazy {
        viewBinding?.root?.let{
            Snackbar.make(
                it,
                R.string.activity_authorization_code_request_error,
                Snackbar.LENGTH_LONG
            )
        }
    }
    private val navigationController by lazy{
        findNavController()
    }

    private var viewBinding: FragmentTelephoneNumberInputBinding? = null

    lateinit var phoneInputComponent: PhoneInputComponent
        private set

    @Inject
    lateinit var authorizationViewModel: IAuthorizationViewModel

    @Inject
    lateinit var viewModel: IPhoneInputViewModel

    @Inject
    @MainThreadScheduler
    lateinit var mainThreadScheduler: Scheduler

    @Inject
    @ComputationScheduler
    lateinit var computationScheduler: Scheduler

    override fun onAttach(context: Context) {
        super.onAttach(context)
        createTelephoneNumberInputComponent()
        injectDependencies()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding = FragmentTelephoneNumberInputBinding.bind(view)
        initializeTelephoneNumberEditText()
        initializeSendCodeButton()
        initializeNewCodeTimerText()
        initializeCodeRequestErrorSnackbar()
        connectAuthorizationViewModel()
        manageNavigation()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewBinding = null
    }

    private fun createTelephoneNumberInputComponent(){
        phoneInputComponent = authorizationComponent.phoneInputComponent().create(this)
    }

    private fun injectDependencies(){
        phoneInputComponent.inject(this)
    }

    private fun connectAuthorizationViewModel(){
        viewModel.updatePhoneNumber(authorizationViewModel.phoneNumber.value ?: "")
        viewModel.phoneNumber.observe(
            viewLifecycleOwner,
            authorizationViewModel::updatePhoneNumber
        )
    }

    private fun initializeTelephoneNumberEditText(){
        viewBinding?.phoneNumberEditText?.apply {
            setText(viewModel.phoneNumber.value ?: "")
            addTextChangedListener(
                TelephoneNumberEditTextTextWatcher(PhoneNumberFormattingTextWatcher())
            )
        }
    }

    private fun initializeSendCodeButton(){
        viewBinding?.sendCodeButton?.apply{
            viewModel.canUserRequestCode.observe(
                viewLifecycleOwner,
                ::setEnabled
            )

            clicks()
                .throttleLatest(2, TimeUnit.SECONDS, computationScheduler)
                .observeOn(mainThreadScheduler)
                .subscribe{
                    viewModel.requestVerificationCode()
                }
                .addTo(viewScopeDisposables)
        }
    }

    private fun initializeNewCodeTimerText(){
        viewModel.isCodeRequestAvailable.observe(
            viewLifecycleOwner,
            ::manageNewCodeTimerTextVisibility
        )

        viewModel.newCodeRequestWaitingTime.observe(
            viewLifecycleOwner,
            ::updateNewCodeWaitingTimeText
        )
    }

    private fun manageNewCodeTimerTextVisibility(isCodeRequestAvailable: Boolean){
        viewBinding?.newCodeTimerText?.apply{
            visibility = if(isCodeRequestAvailable){
                View.INVISIBLE
            } else{
                View.VISIBLE
            }
        }
    }

    private fun manageNavigation(){
        viewModel.isVerificationCodeSent.observe(viewLifecycleOwner, { isVerificationCodeSent ->
            if(isVerificationCodeSent == true){
                val action = TelephoneNumberInputFragmentDirections.actionTelephoneNumberInputDestinationToTelephoneNumberConfirmationDestination()
                navigationController.navigate(action)
            }
        })
    }

    private fun initializeCodeRequestErrorSnackbar(){
        viewModel.isVerificationCodeSent.observe(viewLifecycleOwner, { isVerificationCodeSent ->
            if(isVerificationCodeSent == false){
                codeRequestErrorSnackbar?.show()
            }
        })
    }

    private fun updateNewCodeWaitingTimeText(timeLeft: Long){
        viewBinding?.newCodeTimerText?.apply {
            val date = Date(timeLeft * 1000)
            val formattedTime = SimpleDateFormat("mm:ss", Locale.getDefault()).format(date)

            val originText = getString(R.string.fragment_telephone_number_input_new_code_message)
            val textWithSubstitutions = originText.format(formattedTime)

            val phoneTextStart = originText.indexOfFirst{it == '%'}
            val phoneTextEnd = phoneTextStart + formattedTime.toString().length

            val phoneTextColor = ContextCompat.getColor(context,R.color.colorPrimary)

            val styledText = SpannableString(textWithSubstitutions).apply {
                setSpan(
                    ForegroundColorSpan(phoneTextColor),
                    phoneTextStart,
                    phoneTextEnd,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }

            text = styledText
        }
    }

    private inner class TelephoneNumberEditTextTextWatcher(
        private val phoneNumberFormattingTextWatcher: PhoneNumberFormattingTextWatcher
    ) : TextWatcher by phoneNumberFormattingTextWatcher{
        override fun afterTextChanged(s: Editable?) {
            phoneNumberFormattingTextWatcher.afterTextChanged(s)
            val newText = s?.toString() ?: ""
            viewModel.updatePhoneNumber(newText)
        }
    }
}