package tv.wfc.livestreamsales.features.authorization.paymentcardinformation.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import tv.wfc.livestreamsales.R
import tv.wfc.livestreamsales.application.ui.base.BaseFragment
import tv.wfc.livestreamsales.databinding.FragmentRegistrationPaymentCardInformationBinding
import tv.wfc.livestreamsales.features.authorization.paymentcardinformation.di.RegistrationPaymentCardInformationComponent
import tv.wfc.livestreamsales.features.authorization.paymentcardinformation.viewmodel.IRegistrationPaymentCardInformationViewModel
import javax.inject.Inject

class RegistrationPaymentCardInformationFragment: BaseFragment(R.layout.fragment_registration_payment_card_information) {
    private val navigationController by lazy { findNavController() }

    private var viewBinding: FragmentRegistrationPaymentCardInformationBinding? = null

    private lateinit var registrationPaymentCardInformationComponent: RegistrationPaymentCardInformationComponent

    @Inject
    lateinit var viewModel: IRegistrationPaymentCardInformationViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        createRegistrationPaymentCardInformationComponent()
        injectDependencies()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindView(view)
        initializeContentLoader()
    }

    override fun onDestroyView() {
        unbindView()
        super.onDestroyView()
    }

    private fun createRegistrationPaymentCardInformationComponent(){
        val authorizationComponent = appComponent
            .authorizationComponent()
            .create()

        registrationPaymentCardInformationComponent = authorizationComponent
            .registrationPaymentCardInformationComponent()
            .create(this)
    }

    private fun injectDependencies(){
        registrationPaymentCardInformationComponent.inject(this)
    }

    private fun bindView(view: View){
        viewBinding = FragmentRegistrationPaymentCardInformationBinding.bind(view)
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

    }
}