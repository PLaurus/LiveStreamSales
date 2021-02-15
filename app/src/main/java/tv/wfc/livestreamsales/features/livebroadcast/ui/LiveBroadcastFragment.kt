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
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import tv.wfc.livestreamsales.R
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.ComputationScheduler
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.MainThreadScheduler
import tv.wfc.livestreamsales.application.tools.errors.IApplicationErrorsLogger
import tv.wfc.livestreamsales.application.tools.view.hideSmoothly
import tv.wfc.livestreamsales.application.tools.view.matchRootView
import tv.wfc.livestreamsales.application.tools.view.revealSmoothly
import tv.wfc.livestreamsales.databinding.FragmentLiveBroadcastBinding
import tv.wfc.livestreamsales.features.authorizeduser.ui.base.AuthorizedUserFragment
import tv.wfc.livestreamsales.features.livebroadcast.di.LiveBroadcastComponent
import tv.wfc.livestreamsales.features.livebroadcast.viewmodel.ILiveBroadcastViewModel
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class LiveBroadcastFragment: AuthorizedUserFragment(R.layout.fragment_live_broadcast) {
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
    override lateinit var viewModel: ILiveBroadcastViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        initializeLiveBroadcastComponent()
        injectDependencies()
        prepareViewModel(navigationArguments.liveBroadcastId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindView(view)
    }

    override fun onDataIsPrepared() {
        super.onDataIsPrepared()
        initializeImage()
        initializeBroadcastTitleText()
        initializeViewersCountText()
        initializeBroadcastDescriptionText()
        initializePlayerView()
        initializeBuyButton()
        initializeSendMessageButton()
        initializeMessageInput()
        showBroadcastInformationTemporarily()
    }

    override fun onStart() {
        super.onStart()
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
        super.onStop()
        if (Util.SDK_INT > 23) {
            pausePlayerLifecycle()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unbindView()
    }

    private fun initializeLiveBroadcastComponent(){
        liveBroadcastComponent = authorizedUserComponent.liveBroadcastComponent().create(this)
    }

    private fun injectDependencies(){
        liveBroadcastComponent.inject(this)
    }

    private fun prepareViewModel(broadcastId: Long){
        viewModel.prepareData(broadcastId)
    }

    private fun bindView(view: View){
        viewBinding = FragmentLiveBroadcastBinding.bind(view)
    }

    private fun unbindView(){
        viewBinding = null
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
        viewBinding?.buyButton?.apply{
            setOnClickListener { navigateToProductOrderDestination() }
        }
    }

    private fun initializeSendMessageButton(){
        viewBinding?.sendMessageButton?.apply{

        }
    }

    private fun initializeMessageInput(){
        viewBinding?.run{
            messageInput.setOnFocusChangeListener { v, hasFocus ->
                if(hasFocus){
                    buyButton.visibility = View.INVISIBLE
                    sendMessageButton.visibility = View.VISIBLE
                } else{
                    sendMessageButton.visibility = View.INVISIBLE
                    buyButton.visibility = View.VISIBLE
                }
            }
        }

    }

    private fun resumePlayerLifecycle(){
        initializePlayer()
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
            .timer(5L, TimeUnit.SECONDS, computationScheduler)
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
        val action = LiveBroadcastFragmentDirections.actionLiveBroadcastDestinationToProductOrderDestination()
        navigationController.navigate(action)
    }
}