package com.example.livestreamsales.ui.fragment.telephonenumberconfirmation

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.*
import android.text.Annotation
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.text.getSpans
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.example.livestreamsales.R
import com.example.livestreamsales.databinding.FragmentPhoneConfirmationBinding
import com.example.livestreamsales.di.components.app.modules.reactivex.qualifiers.MainThreadScheduler
import com.example.livestreamsales.di.components.phoneconfirmation.PhoneConfirmationComponent
import com.example.livestreamsales.model.application.phoneconfirmation.PhoneConfirmationResult
import com.example.livestreamsales.model.application.viewmodel.ViewModelPreparationState
import com.example.livestreamsales.ui.fragment.base.AuthorizationFragment
import com.example.livestreamsales.utils.IStringResAnnotationProcessor
import com.example.livestreamsales.viewmodels.authorization.IAuthorizationViewModel
import com.example.livestreamsales.viewmodels.phoneconfirmation.IPhoneConfirmationViewModel
import com.jakewharton.rxbinding4.view.clicks
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.kotlin.addTo
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class TelephoneNumberConfirmationFragment: AuthorizationFragment(R.layout.fragment_phone_confirmation) {
    private val navigationController by lazy{
        findNavController()
    }

    private var viewBinding: FragmentPhoneConfirmationBinding? = null

    lateinit var phoneConfirmationComponent: PhoneConfirmationComponent
        private set

    @Inject
    lateinit var authorizationViewModel: IAuthorizationViewModel

    @Inject
    lateinit var viewModel: IPhoneConfirmationViewModel

    @Inject
    @MainThreadScheduler
    lateinit var mainThreadScheduler: Scheduler

    @Inject
    lateinit var stringResAnnotationProcessor: IStringResAnnotationProcessor

    override fun onAttach(context: Context) {
        super.onAttach(context)
        createTelephoneNumberConfirmationComponent()
        injectDependencies()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindView(view)
        manageContentState{
            manageNavigation()
            initializeOperationProgressView()
            initializeCodeIsSentText()
            initializeCodeEditText()
            initializeNewCodeTimerText()
            initializeRequestCodeButton()
            initializeTermsOfTheOfferText()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unbindView()
    }

    private fun createTelephoneNumberConfirmationComponent(){
        phoneConfirmationComponent = authorizationComponent
            .phoneConfirmationComponent()
            .create(this)
    }

    private fun injectDependencies(){
        phoneConfirmationComponent.inject(this)
    }

    private fun bindView(view: View){
        viewBinding = FragmentPhoneConfirmationBinding.bind(view)
    }

    private fun unbindView(){
        viewBinding = null
    }

    private fun manageContentState(onContentPrepared: (() -> Unit)? = null){
        viewModel.dataPreparationState.observe(viewLifecycleOwner,{ dataPreparationState ->
            when(dataPreparationState){
                is ViewModelPreparationState.DataIsBeingPrepared ->
                    showContentLoadingProgress()
                is ViewModelPreparationState.DataIsPrepared ->{
                    onContentPrepared?.invoke()
                    showContent()
                }
                is ViewModelPreparationState.FailedToPrepareData ->
                    showContentLoadingError()
                else -> Unit
            }
        })
    }

    private fun manageNavigation(){
        viewModel.phoneConfirmationResult.observe(viewLifecycleOwner,{ phoneConfirmationResult ->
            if(phoneConfirmationResult is PhoneConfirmationResult.PhoneIsConfirmed){
                val action = TelephoneNumberConfirmationFragmentDirections.actionTelephoneNumberConfirmationDestinationToMainGraphDestination()
                navigationController.navigate(action)
            }
        })
    }

    private fun initializeOperationProgressView(){
        viewModel.isCodeBeingChecked.observe(viewLifecycleOwner,{ isCodeBeingChecked ->
            if(isCodeBeingChecked){
                showOperationProgress()
            } else
                showContent()
        })
    }

    private fun initializeCodeIsSentText(){
        authorizationViewModel.phoneNumber.observe(
            viewLifecycleOwner,
            { phoneNumber ->
                viewModel.updatePhoneNumber(phoneNumber)
            }
        )

        viewModel.phoneNumber.observe(
            viewLifecycleOwner,
            { phoneNumber ->
                substituteAndStylePhoneNumberTextToCodeIsSentTextView(phoneNumber)
            }
        )
    }

    private fun initializeCodeEditText(){
        viewBinding?.verificationCodeEditText?.apply {
            text = viewModel.code.value.toString()

            viewModel.codeLength.observe(viewLifecycleOwner,{
                numberOfBoxes = it
            })

            addTextChangedListener {
                viewModel.updateCode(it.toString())
            }

            onMaxTextEntered = {
                viewModel.confirmPhone()
            }
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

    private fun initializeRequestCodeButton(){
        viewModel.isCodeRequestAvailable.observe(
            viewLifecycleOwner,
            ::manageRequestCodeButtonVisibility
        )

        setRequestCodeButtonClickListener(viewModel::requestNewCode)
    }

    private fun initializeTermsOfTheOfferText(){
        viewBinding?.termsOfTheOfferText?.apply {
            makeTextViewLinkable()
            removeHighlight()
        }

        viewModel.termsOfTheOfferUrl.observe(
            viewLifecycleOwner,
            ::applyStyleToTermsOfTheOfferText
        )
    }

    private fun substituteAndStylePhoneNumberTextToCodeIsSentTextView(
        phoneNumber: String
    ){
        viewBinding?.codeIsSentText?.apply {
            val notFormattedText = getString(R.string.fragment_telephone_number_confirmation_code_is_sent)
            val formattedText = notFormattedText.format(phoneNumber)

            val phoneTextStart = notFormattedText.indexOfFirst{it == '%'}
            val phoneTextEnd = phoneTextStart + phoneNumber.length

            val phoneTextColor = ContextCompat.getColor(context,R.color.colorPrimary)

            val styledText = SpannableString(formattedText).apply {
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

    private fun manageNewCodeTimerTextVisibility(isCodeRequestAvailable: Boolean){
        viewBinding?.newCodeTimerText?.apply{
            visibility = if(isCodeRequestAvailable){
                View.INVISIBLE
            } else{
                View.VISIBLE
            }
        }
    }

    private fun setRequestCodeButtonClickListener(onClick: () -> Unit){
        viewBinding?.requestNewCodeButton?.apply {
            if(hasOnClickListeners())
                return

            clicks()
                .throttleLatest(2L, TimeUnit.SECONDS)
                .subscribeOn(mainThreadScheduler)
                .observeOn(mainThreadScheduler)
                .subscribe {
                    onClick()
                }
                .addTo(viewScopeDisposables)
        }
    }

    private fun manageRequestCodeButtonVisibility(isCodeRequestAvailable: Boolean){
        viewBinding?.requestNewCodeButton?.apply{
            visibility = if(isCodeRequestAvailable){
                View.VISIBLE
            } else{
                View.INVISIBLE
            }
        }
    }

    private fun TextView.makeTextViewLinkable(){
        movementMethod = LinkMovementMethod.getInstance()
    }

    private fun TextView.removeHighlight(){
        highlightColor = Color.TRANSPARENT
    }

    private fun applyStyleToTermsOfTheOfferText(url: String){
        viewBinding?.termsOfTheOfferText?.apply {
            val spannedText = getText(R.string.fragment_phone_confirmation_terms_of_the_offer_text) as SpannedString
            val spannableText = SpannableString(spannedText)
            val annotations = spannedText.getSpans<Annotation>(0, spannedText.length)

            val highlightedTextColor = ContextCompat.getColor(context,R.color.colorPrimary)

            annotations.forEach { annotation ->
                spannableText.apply {
                    setSpan(
                        ForegroundColorSpan(highlightedTextColor),
                        spannedText.getSpanStart(annotation),
                        spannedText.getSpanEnd(annotation),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )

                    setSpan(
                        getTermsOfTheOfferLinkSpan(context, url),
                        spannedText.getSpanStart(annotation),
                        spannedText.getSpanEnd(annotation),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
            }

            text = spannableText
        }
    }

    private fun updateNewCodeWaitingTimeText(timeLeft: Long){
        viewBinding?.newCodeTimerText?.apply {
            val date = Date(timeLeft * 1000)
            val formattedTime = SimpleDateFormat("mm:ss", Locale.getDefault()).format(date)

            val originText = getString(R.string.fragment_telephone_number_confirmation_new_code_message)
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

    private fun getTermsOfTheOfferLinkSpan(context: Context, url: String) = object: ClickableSpan(){
        override fun onClick(widget: View) {
            val webPage = Uri.parse(url)
            val webPageIntent = Intent(Intent.ACTION_VIEW, webPage)

            if(webPageIntent.resolveActivity(context.packageManager) != null){
                startActivity(webPageIntent)
            }
        }

        override fun updateDrawState(ds: TextPaint) {
            ds.isUnderlineText = false
        }
    }
}