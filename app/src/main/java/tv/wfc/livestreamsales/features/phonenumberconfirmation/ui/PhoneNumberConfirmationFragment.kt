package tv.wfc.livestreamsales.features.phonenumberconfirmation.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.*
import android.text.Annotation
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.text.getSpans
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.jakewharton.rxbinding4.view.clicks
import com.laurus.p.tools.view.makeTextViewLinkable
import com.laurus.p.tools.view.removeHighlight
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.kotlin.addTo
import tv.wfc.livestreamsales.R
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.MainThreadScheduler
import tv.wfc.livestreamsales.application.model.phonenumberconfirmation.PhoneNumberConfirmationResult
import tv.wfc.livestreamsales.application.tools.stringresannotation.IStringResAnnotationProcessor
import tv.wfc.livestreamsales.databinding.FragmentPhoneNumberConfirmationBinding
import tv.wfc.livestreamsales.features.login.ui.base.LogInFragment
import tv.wfc.livestreamsales.features.phonenumberconfirmation.di.PhoneNumberConfirmationComponent
import tv.wfc.livestreamsales.features.phonenumberconfirmation.viewmodel.IPhoneNumberConfirmationViewModel
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class PhoneNumberConfirmationFragment: LogInFragment(R.layout.fragment_phone_number_confirmation) {
    private val navigationController by lazy{
        findNavController()
    }

    private var viewBinding: FragmentPhoneNumberConfirmationBinding? = null

    lateinit var phoneNumberConfirmationComponent: PhoneNumberConfirmationComponent
        private set

    @Inject
    override lateinit var viewModel: IPhoneNumberConfirmationViewModel

    @Inject
    @MainThreadScheduler
    lateinit var mainThreadScheduler: Scheduler

    @Inject
    lateinit var stringResAnnotationProcessor: IStringResAnnotationProcessor

    override fun onAttach(context: Context) {
        super.onAttach(context)
        createPhoneNumberConfirmationComponent()
        injectDependencies()
    }

    override fun onContentViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onContentViewCreated(view, savedInstanceState)
        bindView(view)
    }

    override fun onDataIsPrepared() {
        super.onDataIsPrepared()
        manageNavigation()
        initializeOperationProgressView()
        initializeCodeIsSentText()
        initializeCodeEditText()
        initializeNewCodeTimerText()
        initializeRequestCodeButton()
        initializeTermsOfTheOfferText()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unbindView()
    }

    private fun createPhoneNumberConfirmationComponent(){
        phoneNumberConfirmationComponent = logInComponent
            .phoneNumberConfirmationComponent()
            .create(this)
    }

    private fun injectDependencies(){
        phoneNumberConfirmationComponent.inject(this)
    }

    private fun bindView(view: View){
        viewBinding = FragmentPhoneNumberConfirmationBinding.bind(view)
    }

    private fun unbindView(){
        viewBinding = null
    }

    private fun manageNavigation(){
        viewModel.phoneNumberConfirmationResult.observe(viewLifecycleOwner,{ phoneNumberConfirmationResult ->
            if(phoneNumberConfirmationResult is PhoneNumberConfirmationResult.PhoneNumberIsConfirmed){
                val action = PhoneNumberConfirmationFragmentDirections.actionPhoneNumberConfirmationDestinationToMainGraphDestination()
                navigationController.navigate(action)
                requireActivity().finish()
            }
        })
    }

    private fun initializeOperationProgressView(){
        viewModel.isCodeBeingChecked.observe(viewLifecycleOwner,{ isCodeBeingChecked ->
            if(isCodeBeingChecked){
                showOperationProgress()
            } else{
                hideOperationProgress()
            }
        })
    }

    private fun initializeCodeIsSentText(){
        viewModel.phoneNumber.observe(
            viewLifecycleOwner,
            { phoneNumber ->
                substituteAndStylePhoneNumberTextToCodeIsSentTextView(phoneNumber)
            }
        )
    }

    private fun initializeCodeEditText(){
        viewBinding?.confirmationCodeEditText?.apply {
            text = viewModel.code.value.toString()

            viewModel.codeLength.observe(viewLifecycleOwner,{
                numberOfBoxes = it
            })

            addTextChangedListener {
                viewModel.updateCode(it.toString())
            }

            onMaxTextEntered = {
                viewModel.confirmPhoneNumber()
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
            val notFormattedText = getString(R.string.fragment_phone_number_confirmation_code_is_sent)
            val formattedText = notFormattedText.format(phoneNumber)

            val phoneNumberTextStart = notFormattedText.indexOfFirst{it == '%'}
            val phoneNumberTextEnd = phoneNumberTextStart + phoneNumber.length

            val phoneNumberTextColor = ContextCompat.getColor(context, R.color.colorPrimary)

            val styledText = SpannableString(formattedText).apply {
                setSpan(
                    ForegroundColorSpan(phoneNumberTextColor),
                    phoneNumberTextStart,
                    phoneNumberTextEnd,
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

    private fun applyStyleToTermsOfTheOfferText(url: String){
        viewBinding?.termsOfTheOfferText?.apply {
            val spannedText = getText(R.string.fragment_phone_number_confirmation_terms_of_the_offer_text) as SpannedString
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

            val originText = getString(R.string.fragment_phone_number_confirmation_new_code_message)
            val textWithSubstitutions = originText.format(formattedTime)

            val phoneNumberTextStart = originText.indexOfFirst{it == '%'}
            val phoneNumberTextEnd = phoneNumberTextStart + formattedTime.toString().length

            val phoneNumberTextColor = ContextCompat.getColor(context,R.color.colorPrimary)

            val styledText = SpannableString(textWithSubstitutions).apply {
                setSpan(
                    ForegroundColorSpan(phoneNumberTextColor),
                    phoneNumberTextStart,
                    phoneNumberTextEnd,
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