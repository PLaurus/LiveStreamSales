package com.example.livestreamsales.ui.fragment.telephonenumberinput

import android.content.Context
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.example.livestreamsales.R
import com.example.livestreamsales.databinding.FragmentTelephoneNumberInputBinding
import com.example.livestreamsales.di.components.app.ReactiveXModule
import com.example.livestreamsales.di.components.telephonenumberinput.TelephoneNumberInputComponent
import com.example.livestreamsales.ui.fragment.base.AuthorizationFragment
import com.example.livestreamsales.viewmodels.telephonenumberinput.ITelephoneNumberInputViewModel
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding4.view.clicks
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Named

class TelephoneNumberInputFragment: AuthorizationFragment(R.layout.fragment_telephone_number_input) {
    private val disposables = CompositeDisposable()
    private val codeRequestErrorSnackbar: Snackbar? by lazy {
        viewBinding?.root?.let{
            Snackbar.make(
                it,
                R.string.fragment_telephone_number_input_code_request_error,
                Snackbar.LENGTH_LONG
            )
        }
    }

    private var viewBinding: FragmentTelephoneNumberInputBinding? = null

    lateinit var telephoneNumberInputComponent: TelephoneNumberInputComponent
        private set

    @Inject
    lateinit var viewModel: ITelephoneNumberInputViewModel

    @Inject
    @Named(ReactiveXModule.DEPENDENCY_NAME_MAIN_THREAD_SCHEDULER)
    lateinit var mainThreadScheduler: Scheduler

    @Inject
    @Named(ReactiveXModule.DEPENDENCY_NAME_COMPUTATION_SCHEDULER)
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
        manageNavigation()
        observeCodeRequestError()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewBinding = null
        disposables.dispose()
    }

    private fun createTelephoneNumberInputComponent(){
        telephoneNumberInputComponent = authorizationComponent.telephoneNumberInputComponent().create(this)
    }

    private fun injectDependencies(){
        telephoneNumberInputComponent.inject(this)
    }

    private fun initializeTelephoneNumberEditText(){
        viewBinding?.phoneNumberEditText?.addTextChangedListener(
            TelephoneNumberEditTextTextWatcher(PhoneNumberFormattingTextWatcher())
        )
    }

    private fun initializeSendCodeButton(){
        viewBinding?.sendCodeButton?.apply{
            clicks()
                .throttleLatest(2, TimeUnit.SECONDS, computationScheduler)
                .subscribeOn(mainThreadScheduler)
                .observeOn(mainThreadScheduler)
                .subscribe{ viewModel.requestVerificationCode() }
                .addTo(disposables)

            viewModel.isTelephoneNumberCorrect.observe(viewLifecycleOwner, { isPhoneNumberCorrect ->
                isEnabled = isPhoneNumberCorrect
            })
        }
    }

    private fun manageNavigation(){
        viewModel.isVerificationCodeSent.observe(viewLifecycleOwner, { isVerificationCodeSent ->
            if(isVerificationCodeSent){
                TelephoneNumberInputFragmentDirections.actionTelephoneNumberInputDestinationToTelephoneNumberConfirmationDestination()
            }
        })
    }

    private fun observeCodeRequestError(){
        viewModel.isVerificationCodeSent.observe(viewLifecycleOwner, { isVerificationCodeSent ->
            if(!isVerificationCodeSent){
                codeRequestErrorSnackbar?.show()
            }
        })
    }

    private inner class TelephoneNumberEditTextTextWatcher(
        private val phoneNumberFormattingTextWatcher: PhoneNumberFormattingTextWatcher
    ) : TextWatcher by phoneNumberFormattingTextWatcher{
        override fun afterTextChanged(s: Editable?) {
            phoneNumberFormattingTextWatcher.afterTextChanged(s)
            viewModel.updatePhoneNumber(s?.toString() ?: "")
        }
    }
}