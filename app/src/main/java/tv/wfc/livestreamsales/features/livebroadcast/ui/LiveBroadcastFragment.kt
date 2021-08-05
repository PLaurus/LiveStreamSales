package tv.wfc.livestreamsales.features.livebroadcast.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.RenderersFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.source.MediaSourceFactory
import com.google.android.exoplayer2.trackselection.TrackSelector
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.util.ErrorMessageProvider
import com.google.android.exoplayer2.util.Util
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding4.view.clicks
import com.laurus.p.tools.view.hideSmoothly
import com.laurus.p.tools.view.matchRootView
import com.laurus.p.tools.view.revealSmoothly
import com.laurus.p.tools.viewpager2.goToNextPage
import com.laurus.p.tools.viewpager2.goToPreviousPage
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import tv.wfc.livestreamsales.NavigationGraphRootDirections
import tv.wfc.livestreamsales.R
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.ComputationScheduler
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.MainThreadScheduler
import tv.wfc.livestreamsales.application.model.chat.ChatMessage
import tv.wfc.livestreamsales.application.model.products.ProductGroup
import tv.wfc.livestreamsales.application.tools.errors.IApplicationErrorsLogger
import tv.wfc.livestreamsales.application.ui.base.BaseFragment
import tv.wfc.livestreamsales.databinding.FragmentLiveBroadcastBinding
import tv.wfc.livestreamsales.features.livebroadcast.di.LiveBroadcastComponent
import tv.wfc.livestreamsales.features.livebroadcast.ui.adapters.messages.MessagesAdapter
import tv.wfc.livestreamsales.features.livebroadcast.ui.adapters.products.ProductsAdapter
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
    lateinit var productsDiffUtilItemCallback: DiffUtil.ItemCallback<ProductGroup>

    @Inject
    lateinit var chatMessagesDiffUtilItemCallback: DiffUtil.ItemCallback<ChatMessage>

    @Inject
    lateinit var imageLoader: ImageLoader

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
        liveBroadcastComponent = appComponent.liveBroadcastComponent().create(this)
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
        initializePreviousProductButton()
        initializeProductsViewPager()
        initializeNextProductButton()
        initializePlayer()
        initializePlayerView()
        initializeErrorNoBroadcastManifestText()
        initializeBuyButton()
        initializeSendMessageButton()
        initializeMessageInput()
        initializeChatRecyclerView()
        initializeBottomLayout()
        initializeSnackBar()
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

    private fun initializePreviousProductButton(){
        viewBinding?.run{
            viewModel.broadcastHasProducts.observe(viewLifecycleOwner){ hasProducts ->
                previousProductButton.visibility = when(hasProducts){
                    true -> View.VISIBLE
                    else -> View.GONE
                }
            }

            previousProductButton.clicks()
                .throttleLatest(500L, TimeUnit.MILLISECONDS, computationScheduler)
                .observeOn(mainThreadScheduler)
                .subscribeBy(
                    onNext = { productsViewPager.goToPreviousPage() },
                    onError = applicationErrorsLogger::logError
                )
                .addTo(viewScopeDisposables)
        }
    }

    private fun initializeProductsViewPager(){
        viewBinding?.productsViewPager?.run {
            viewModel.broadcastHasProducts.observe(viewLifecycleOwner){ hasProducts ->
                visibility = when(hasProducts){
                    true -> View.VISIBLE
                    else -> View.GONE
                }
            }

            adapter = ProductsAdapter(
                productsDiffUtilItemCallback,
                imageLoader
            )

            (getChildAt(0) as RecyclerView).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

            viewModel.productGroups.observe(viewLifecycleOwner){ products ->
                (adapter as ProductsAdapter).submitList(products)
            }
        }
    }

    private fun initializeNextProductButton(){
        viewBinding?.run{
            viewModel.broadcastHasProducts.observe(viewLifecycleOwner){ hasProducts ->
                nextProductButton.visibility = when(hasProducts){
                    true -> View.VISIBLE
                    else -> View.GONE
                }
            }

            nextProductButton.clicks()
                .throttleLatest(500L, TimeUnit.MILLISECONDS, computationScheduler)
                .observeOn(mainThreadScheduler)
                .subscribeBy(
                    onNext = { productsViewPager.goToNextPage() },
                    onError = applicationErrorsLogger::logError
                )
                .addTo(viewScopeDisposables)
        }
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

    private fun initializeErrorNoBroadcastManifestText(){
        viewBinding?.errorNoBroadcastManifestText?.run{
            viewModel.broadcastMediaItem.observe(viewLifecycleOwner){ mediaItem ->
                visibility = if(mediaItem == null) View.VISIBLE else View.GONE
            }
        }
    }

    private fun initializePlayer() {
        viewModel.broadcastMediaItem.observe(viewLifecycleOwner, { broadcastMediaItem ->
            releasePlayer()

            context?.let {
                val player = SimpleExoPlayer.Builder(it, renderersFactory)
                    .setMediaSourceFactory(mediaSourceFactory)
                    .setTrackSelector(trackSelector)
                    .build()

                player.apply {
                    addListener(viewModel.playerEventListener)
                    setAudioAttributes(AudioAttributes.DEFAULT, true)
                    playWhenReady = true
                    broadcastMediaItem?.let(::setMediaItem)
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
                    onNext = {
                        if(viewModel.isUserLoggedIn.value == true){
                            navigateToProductOrderDestination()
                        } else{
                            navigateToAuthorization()
                        }
                    },
                    onError = applicationErrorsLogger::logError
                )
                .addTo(viewScopeDisposables)
        }
    }

    private fun initializeSendMessageButton(){
        viewBinding?.sendMessageButton?.apply{
            clicks()
                .throttleLatest(500L, TimeUnit.MILLISECONDS, computationScheduler)
                .observeOn(mainThreadScheduler)
                .subscribeBy(
                    onNext = { viewModel.sendMessage() },
                    onError = applicationErrorsLogger::logError
                )
                .addTo(viewScopeDisposables)
        }
    }

    private fun initializeMessageInput(){
        viewBinding?.run{
            messageInput.apply{
                viewModel.enteredMessage.observe(viewLifecycleOwner, Observer{ enteredMessage ->
                    if(text.toString() == enteredMessage) return@Observer

                    setText(enteredMessage, TextView.BufferType.EDITABLE)
                })

                addTextChangedListener { editable ->
                    viewModel.updateEnteredMessage(editable.toString())
                }
            }
        }
    }

    private fun initializeChatRecyclerView(){
        viewBinding?.chatRecyclerView?.run {
            adapter = MessagesAdapter(chatMessagesDiffUtilItemCallback)

            overScrollMode = RecyclerView.OVER_SCROLL_NEVER


            viewModel.chatMessages.observe(viewLifecycleOwner){ messages ->
                (adapter as MessagesAdapter).submitList(messages)
            }
        }
    }

    private fun initializeBottomLayout(){
        viewBinding?.run{
            root.viewTreeObserver.addOnGlobalFocusChangeListener { _, _ ->
                if(bottomLayout.hasFocus()){
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

    private fun initializeSnackBar(){
        viewBinding?.run {
            viewModel.genericErrorEvent.observe(viewLifecycleOwner){ errorMessage ->
                Snackbar.make(root, errorMessage, Snackbar.LENGTH_SHORT).show()
            }
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
            productsLayout.revealSmoothly(broadcastInformationRevealDuration)
        }
    }

    private fun hideBroadcastInformation(){
        viewBinding?.apply {
            headerLayout.hideSmoothly(broadcastInformationHideDuration)
            productsLayout.hideSmoothly(broadcastInformationHideDuration)
        }
    }

    private fun navigateToProductOrderDestination(){
        val action = LiveBroadcastFragmentDirections.actionLiveBroadcastDestinationToProductOrderDestination(navigationArguments.liveBroadcastId)
        navigationController.navigate(action)
    }

    private fun navigateToAuthorization(){
        val action = NavigationGraphRootDirections.actionGlobalPhoneNumberInputDestination()
        navigationController.navigate(action)
    }
}