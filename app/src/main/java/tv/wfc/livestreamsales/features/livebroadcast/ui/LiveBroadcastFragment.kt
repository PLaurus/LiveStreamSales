package tv.wfc.livestreamsales.features.livebroadcast.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.RenderersFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.source.MediaSourceFactory
import com.google.android.exoplayer2.trackselection.TrackSelector
import com.google.android.exoplayer2.util.ErrorMessageProvider
import com.google.android.exoplayer2.util.Util
import tv.wfc.livestreamsales.R
import tv.wfc.livestreamsales.application.tools.view.matchRootView
import tv.wfc.livestreamsales.databinding.FragmentLiveBroadcastBinding
import tv.wfc.livestreamsales.features.authorizeduser.ui.base.AuthorizedUserFragment
import tv.wfc.livestreamsales.features.livebroadcast.di.LiveBroadcastComponent
import tv.wfc.livestreamsales.features.livebroadcast.viewmodel.ILiveBroadcastViewModel
import javax.inject.Inject

class LiveBroadcastFragment: AuthorizedUserFragment(R.layout.fragment_live_broadcast) {
    private val navigationArguments by navArgs<LiveBroadcastFragmentArgs>()
    private val navigationController by lazy { findNavController() }

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
            requestFocus()
        }
    }

    private fun initializePlayer() {
        viewModel.broadcastMediaItem.observe(viewLifecycleOwner, mediaItemObserver)
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
        viewModel.broadcastMediaItem.removeObserver(mediaItemObserver)
        player?.release()
        player = null
    }

    private fun navigateUp(){
        navigationController.navigateUp()
    }

    private val mediaItemObserver = object: Observer<MediaItem?>{
        override fun onChanged(broadcastMediaItem: MediaItem?) {
            releasePlayer()

            if (broadcastMediaItem == null) {
                navigateUp()
                return
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
        }
    }
}