package tv.wfc.livestreamsales.features.livebroadcast.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.RenderersFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.source.MediaSourceFactory
import com.google.android.exoplayer2.trackselection.TrackSelector
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.util.ErrorMessageProvider
import com.google.android.exoplayer2.util.Util
import com.jakewharton.rxbinding4.view.clicks
import com.laurus.p.tools.string.strikeThrough
import com.laurus.p.tools.view.hideSmoothly
import com.laurus.p.tools.view.matchRootView
import com.laurus.p.tools.view.revealSmoothly
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import tv.wfc.livestreamsales.R
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.ComputationScheduler
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.MainThreadScheduler
import tv.wfc.livestreamsales.application.tools.errors.IApplicationErrorsLogger
import tv.wfc.livestreamsales.application.ui.base.BaseFragment
import tv.wfc.livestreamsales.databinding.FragmentLiveBroadcastBinding
import tv.wfc.livestreamsales.features.livebroadcast.di.LiveBroadcastComponent
import tv.wfc.livestreamsales.features.livebroadcast.viewmodel.ILiveBroadcastViewModel
import tv.wfc.livestreamsales.features.mainappcontent.ui.MainAppContentActivity
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class LiveBroadcastFragment: BaseFragment(R.layout.fragment_live_broadcast) {
    private val navigationArguments by navArgs<LiveBroadcastFragmentArgs>()
    private val navigationController by lazy { findNavController() }
    private val broadcastInformationRevealDuration by lazy{
        resources.getInteger(android.R.integer.config_mediumAnimTime).toLong()
    }
    private val broadcastInformationHideDuration by lazy{
        resources.getInteger(android.R.integer.config_longAnimTime).toLong()
    }

    private var viewBinding: FragmentLiveBroadcastBinding? = null
    private var player: SimpleExoPlayer? = null

    private lateinit var liveBroadcastComponent: LiveBroadcastComponent

    @Inject
    lateinit var playerErrorMessageProvider: ErrorMessageProvider<ExoPlaybackException>

    @Inject
    lateinit var renderersFactory: RenderersFactory

    @Inject
    lateinit var mediaSourceFactory: MediaSourceFactory

    @Inject
    lateinit var trackSelector: TrackSelector

    @Inject
    @ComputationScheduler
    lateinit var computationScheduler: Scheduler

    @Inject
    @MainThreadScheduler
    lateinit var mainThreadScheduler: Scheduler

    @Inject
    lateinit var applicationErrorsLogger: IApplicationErrorsLogger

    @Inject
    lateinit var viewModel: ILiveBroadcastViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        initializeLiveBroadcastComponent()
        injectDependencies()
        prepareViewModel(navigationArguments.liveBroadcastId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindView(view)
        initializeContentLoader()
    }

    override fun onStart() {
        super.onStart()
        (requireActivity() as MainAppContentActivity).hideToolbar()

        if(Util.SDK_INT > 23){
            resumePlayerLifecycle()
        }
    }

    override fun onResume() {
        super.onResume()
        if (Util.SDK_INT <= 23){
            resumePlayerLifecycle()
        }
    }

    override fun onPause() {
        super.onPause()
        if (Util.SDK_INT <= 23) {
            pausePlayerLifecycle()
        }
    }

    override fun onStop() {
        (requireActivity() as MainAppContentActivity).showToolbar()

        if (Util.SDK_INT > 23) {
            pausePlayerLifecycle()
        }
        super.onStop()
    }

    override fun onDestroyView() {
        unbindView()
        super.onDestroyView()
    }

    private fun initializeLiveBroadcastComponent(){
        liveBroadcastComponent = authorizedUserComponent
            ?.liveBroadcastComponent()
            ?.create(this)
            ?: appComponent
                .liveBroadcastComponent()
                .create(this)
    }

    private fun injectDependencies(){
        liveBroadcastComponent.inject(this)
    }

    private fun bindView(view: View){
        viewBinding = FragmentLiveBroadcastBinding.bind(view)
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
        initializeImage()
        initializeBroadcastTitleText()
        initializeViewersCountText()
        initializeBroadcastDescriptionText()
        initializePlayer()
        initializePlayerView()
        initializeBuyButton()
        initializeSendMessageButton()
        initializeMessageInput()
        initializePriceText()
        initializeOldPriceText()
        showBroadcastInformationTemporarily()
    }

    private fun prepareViewModel(broadcastId: Long){
        viewModel.prepareData(broadcastId)
    }

    private fun initializeImage(){
        viewModel.image.observe(viewLifecycleOwner, { drawable ->
            viewBinding?.image?.setImageDrawable(drawable)
        })
    }

    private fun initializeBroadcastTitleText(){
        viewModel.broadcastTitle.observe(viewLifecycleOwner, { broadcastTitle ->
            viewBinding?.broadcastTitleText?.text = broadcastTitle
        })
    }

    private fun initializeViewersCountText(){
        viewModel.viewersCount.observe(viewLifecycleOwner, { viewersCount ->
            viewBinding?.viewersIndicatorText?.text = resources.getString(
                R.string.fragment_live_broadcast_viewers_count,
                viewersCount
            )
        })
    }

    private fun initializeBroadcastDescriptionText(){
        viewModel.broadcastDescription.observe(viewLifecycleOwner, { description ->
            viewBinding?.broadcastDescriptionText?.text = description
        })
    }

    private fun initializePlayerView(){
        viewBinding?.playerView?.apply{
            matchRootView()
            setErrorMessageProvider(playerErrorMessageProvider)
            resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
            requestFocus()
            videoSurfaceView?.setOnClickListener {
                showBroadcastInformationTemporarily()
            }
        }
    }

    private fun initializePlayer() {
        viewModel.broadcastMediaItem.observe(viewLifecycleOwner, Observer{ broadcastMediaItem ->
            releasePlayer()

            if (broadcastMediaItem == null) {
                navigateUp()
                return@Observer
            }

            context?.let {
                val player = SimpleExoPlayer.Builder(it, renderersFactory)
                    .setMediaSourceFactory(mediaSourceFactory)
                    .setTrackSelector(trackSelector)
                    .build()

                player.apply {
                    addListener(viewModel.playerEventListener)
                    setAudioAttributes(AudioAttributes.DEFAULT, true)
                    playWhenReady = true
                    setMediaItem(broadcastMediaItem)
                    prepare()
                }

                this@LiveBroadcastFragment.player = player
                viewBinding?.playerView?.player = this@LiveBroadcastFragment.player
            }
        })
    }

    private fun initializeBuyButton(){
        viewBinding?.run{
            viewModel.broadcastHasProducts.observe(viewLifecycleOwner,{ hasProducts ->
                if(!messageInput.isFocused){
                    sendMessageButton.visibility = View.GONE
                    buyButton.visibility = if(hasProducts) View.VISIBLE else View.GONE
                }
            })

            buyButton
                .clicks()
                .throttleFirst(1L, TimeUnit.SECONDS, computationScheduler)
                .observeOn(mainThreadScheduler)
                .subscribeBy(
                    onNext = { navigateToProductOrderDestination() },
                    onError = applicationErrorsLogger::logError
                )
                .addTo(viewScopeDisposables)
        }
    }

    private fun initializeSendMessageButton(){
        viewBinding?.sendMessageButton?.apply{

        }
    }

    private fun initializeMessageInput(){
        viewBinding?.run{
            messageInput.setOnFocusChangeListener { _, hasFocus ->
                if(hasFocus){
                    buyButton.visibility = View.GONE
                    sendMessageButton.visibility = View.VISIBLE
                } else{
                    sendMessageButton.visibility = View.GONE

                    if(viewModel.broadcastHasProducts.value == true) {
                        buyButton.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun initializePriceText(){
        viewBinding?.productPriceText?.apply {
            viewModel.firstProductPrice.observe(viewLifecycleOwner, { price ->
                if(price != null){
                    visibility = View.VISIBLE
                    text = resources.getString(
                        R.string.fragment_live_broadcast_price,
                        price
                    )
                } else{
                    visibility = View.GONE
                }
            })
        }
    }

    private fun initializeOldPriceText(){
        viewBinding?.apply {
            viewModel.firstProductOldPrice.observe(viewLifecycleOwner, { oldPrice ->
                if(oldPrice != null && productPriceText.visibility == View.VISIBLE){
                    productOldPriceText.apply {
                        text = resources.getString(
                            R.string.fragment_live_broadcast_price,
                            oldPrice
                        ).strikeThrough()
                        visibility = View.VISIBLE
                    }
                } else {
                    productOldPriceText.visibility = View.GONE
                }
            })
        }
    }

    private fun resumePlayerLifecycle(){
        viewBinding?.playerView?.onResume()
    }

    private fun pausePlayerLifecycle(){
        viewBinding?.playerView?.onPause()
        releasePlayer()
    }

    private fun releasePlayer() {
        player?.release()
        player = null
    }

    private fun navigateUp(){
        navigationController.navigateUp()
    }

    private var broadcastInformationVisibilityTimerDisposable: Disposable? = null

    private fun showBroadcastInformationTemporarily(){
        broadcastInformationVisibilityTimerDisposable?.dispose()

        broadcastInformationVisibilityTimerDisposable = Observable
            .timer(10L, TimeUnit.SECONDS, computationScheduler)
            .observeOn(mainThreadScheduler)
            .doOnSubscribe { showBroadcastInformation() }
            .subscribeBy(
                onNext = { hideBroadcastInformation() },
                onError = applicationErrorsLogger::logError
            )
            .addTo(viewScopeDisposables)
    }

    private fun showBroadcastInformation(){
        viewBinding?.apply {
            headerLayout.revealSmoothly(broadcastInformationRevealDuration)
            streamInformationLayout.revealSmoothly(broadcastInformationRevealDuration)
        }
    }

    private fun hideBroadcastInformation(){
        viewBinding?.apply {
            headerLayout.hideSmoothly(broadcastInformationHideDuration)
            streamInformationLayout.hideSmoothly(broadcastInformationHideDuration)
        }
    }

    private fun navigateToProductOrderDestination(){
        val action = LiveBroadcastFragmentDirections.actionLiveBroadcastDestinationToProductOrderDestination(navigationArguments.liveBroadcastId)
        navigationController.navigate(action)
    }
}