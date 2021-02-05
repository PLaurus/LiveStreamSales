package tv.wfc.livestreamsales.features.greeting.ui

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.jakewharton.rxbinding4.view.clicks
import tv.wfc.livestreamsales.R
import tv.wfc.livestreamsales.application.tools.errors.IApplicationErrorsLogger
import tv.wfc.livestreamsales.databinding.ActivityGreetingBinding
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.MainThreadScheduler
import tv.wfc.livestreamsales.features.greeting.di.GreetingComponent
import tv.wfc.livestreamsales.features.greeting.model.GreetingPage
import tv.wfc.livestreamsales.application.ui.base.BaseActivity
import tv.wfc.livestreamsales.features.greeting.ui.adapters.greetingpage.GreetingPageViewHolder
import tv.wfc.livestreamsales.features.login.ui.LogInActivity
import tv.wfc.livestreamsales.features.mainappcontent.ui.MainAppContentActivity
import tv.wfc.livestreamsales.features.greeting.viewmodel.IGreetingViewModel
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class GreetingActivity: BaseActivity() {
    private val disposables = CompositeDisposable()

    private val proceedButtonNextString
        get() = resources.getString(R.string.greeting_fragment_next_button)

    private val proceedButtonStartString
        get() = resources.getString(R.string.greeting_fragment_start_button)

    private lateinit var viewBinding: ActivityGreetingBinding

    lateinit var greetingComponent: GreetingComponent
        private set

    @Inject
    lateinit var viewModel: IGreetingViewModel

    @Inject
    @MainThreadScheduler
    lateinit var mainThreadScheduler: Scheduler

    @Inject
    lateinit var applicationErrorsLogger: IApplicationErrorsLogger

    @Inject
    lateinit var greetingPagesAdapter: ListAdapter<GreetingPage, GreetingPageViewHolder>

    override fun onCreate(savedInstanceState: Bundle?) {
        initializeGreetingComponent()
        injectDependencies()
        super.onCreate(savedInstanceState)
        bindView()
        manageNavigation()
        initializePagerView()
        initializePageIndicator()
        initializeProceedButton()
        initializeSkipButton()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }

    private fun initializeGreetingComponent(){
        greetingComponent = appComponent.greetingComponent().create(this)
    }

    private fun injectDependencies(){
        greetingComponent.inject(this)
    }

    private fun bindView(){
        viewBinding = ActivityGreetingBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
    }

    private fun manageNavigation(){
        viewModel.nextDestinationEvent.observe(this){ nextDestination ->
            navigateToNextDestination(nextDestination)
        }
    }

    private fun initializePagerView(){
        viewBinding.pagerView.apply{
            (getChildAt(0) as RecyclerView).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            adapter = greetingPagesAdapter
        }

        viewModel.greetingPages.observe(this){ greetingPages ->
            greetingPagesAdapter.submitList(greetingPages.toList())
        }
    }

    private fun initializePageIndicator(){
        viewBinding.pageIndicator.setViewPager2(viewBinding.pagerView)
    }

    private fun initializeProceedButton(){
        bindProceedButtonToViewPager()
        startProcessingProceedButtonClicks()
    }

    private fun initializeSkipButton(){
        startProcessingSkipButtonClicks()
    }

    private fun bindProceedButtonToViewPager(){
        viewBinding.pagerView.registerOnPageChangeCallback(
            object: ViewPager2.OnPageChangeCallback(){
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    changeProceedButtonText(position)
                }
            }
        )
    }

    private fun changeProceedButtonText(currentPageIndex: Int){
        viewBinding.proceedButton.apply{
            val lastPageIndex = (greetingPagesAdapter.itemCount - 1).coerceAtLeast(0)

            text = if(currentPageIndex >= lastPageIndex){
                proceedButtonStartString
            } else proceedButtonNextString
        }
    }

    private fun startProcessingProceedButtonClicks(){
        viewBinding.apply{
            proceedButton
                .clicks()
                .throttleLatest(500L, TimeUnit.MILLISECONDS)
                .observeOn(mainThreadScheduler)
                .subscribeBy(
                    onNext = { onProceedButtonClick() },
                    onError = applicationErrorsLogger::logError
                )
                .addTo(disposables)
        }
    }

    private fun startProcessingSkipButtonClicks(){
        viewBinding.apply{
            skipButton
                .clicks()
                .firstElement()
                .observeOn(mainThreadScheduler)
                .subscribeBy(
                    onSuccess = { onSkipButtonClick() },
                    onError = applicationErrorsLogger::logError
                )
                .addTo(disposables)
        }
    }

    private fun onProceedButtonClick(){
        val pagerView = viewBinding.pagerView
        val nextPageIndex = pagerView.currentItem + 1
        val lastPageIndex = (greetingPagesAdapter.itemCount - 1).coerceAtLeast(0)

        if(nextPageIndex > lastPageIndex){
            viewModel.notifyGreetingIsShown()
        } else {
            pagerView.currentItem = nextPageIndex
        }
    }

    private fun onSkipButtonClick(){
        viewModel.notifyGreetingIsShown()
    }

    private fun navigateToNextDestination(nextDestination: IGreetingViewModel.Destination){
        when(nextDestination){
            IGreetingViewModel.Destination.LOG_IN -> navigateToLogInActivity()
            IGreetingViewModel.Destination.MAIN_APP_CONTENT -> navigateToMainAppContentActivity()
        }
    }

    private fun navigateToMainAppContentActivity(){
        val mainAppContentActivityIntent = Intent(applicationContext, MainAppContentActivity::class.java)
        startActivity(mainAppContentActivityIntent)
        finish()
    }

    private fun navigateToLogInActivity(){
        val logInActivityIntent = Intent(applicationContext, LogInActivity::class.java)
        startActivity(logInActivityIntent)
        finish()
    }
}