package tv.wfc.livestreamsales.features.productorder.ui.adapters.products

import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.request.Disposable
import coil.request.ImageRequest
import com.laurus.p.tools.context.getDrawableCompat
import tv.wfc.livestreamsales.R
import tv.wfc.livestreamsales.databinding.ListItemProductOrderProductBoxBinding
import tv.wfc.livestreamsales.features.productorder.model.ProductBoxData

class ProductBoxViewHolder(
    view: View,
    private val imageLoader: ImageLoader,
    private val onClick: (holder: ProductBoxViewHolder, productBoxData: ProductBoxData) -> Unit
): RecyclerView.ViewHolder(view) {
    private val viewBinding = ListItemProductOrderProductBoxBinding.bind(view)
    private val context = view.context

    private var imageLoaderDisposable: Disposable? = null

    fun bind(productBoxData: ProductBoxData){
        clearData()
        bindCountInStock(productBoxData.countInStock)
        bindImageUrl(productBoxData.imageUrl)
        initializeOnClickListener(productBoxData)
    }

    fun selectItem(){
        viewBinding.root.alpha = 1.0f
    }

    fun deselectItem(){
        viewBinding.root.alpha = 0.5f
    }

    private fun clearData(){
        clearCountInStockData()
        clearImageData()
        clearClickListener()
    }

    private fun clearCountInStockData(){
        viewBinding.run{
            countInStockText.text = ""
            countInStockLayout.visibility = View.GONE
        }
    }

    private fun clearImageData(){
        imageLoaderDisposable?.dispose()
        viewBinding.imageView.setImageDrawable(null)
    }

    private fun clearClickListener(){
        viewBinding.imageCard.setOnClickListener(null)
    }

    private fun bindCountInStock(countInStock: Int?){
        viewBinding.run{
            if(countInStock == null){
                countInStockLayout.visibility = View.GONE
            } else{
                countInStockLayout.visibility = View.VISIBLE
                countInStockText.text = countInStock.toString()
            }
        }
    }

    private fun bindImageUrl(url: String?){
        if(url == null){
            setImageDefaultDrawable()
        } else{
            val imageRequest = ImageRequest.Builder(context)
                .data(url)
                .target(
                    onError = { setImageDefaultDrawable() },
                    onSuccess = ::setImageDrawable
                )
                .build()

            imageLoaderDisposable?.dispose()
            imageLoaderDisposable = imageLoader.enqueue(imageRequest)
        }
    }

    private fun initializeOnClickListener(productBoxData: ProductBoxData){
        viewBinding.imageCard.setOnClickListener {
            onClick(this, productBoxData)
        }
    }

    private fun setImageDrawable(drawable: Drawable){
        viewBinding.imageView.run {
            setImageDrawable(drawable)
        }
    }

    private fun setImageDefaultDrawable(){
        viewBinding.imageView.run{
            val placeholder = createImageDefaultDrawable()
            setImageDrawable(placeholder)
        }
    }

    private fun createImageDefaultDrawable(): Drawable?{
        return context.getDrawableCompat(
            R.drawable.ic_baseline_shopping_bag_24,
            R.color.productOrder_productBoxItem_placeholderTint
        )
    }
}