package tv.wfc.livestreamsales.features.productorder.viewmodel

import androidx.lifecycle.LiveData
import tv.wfc.contentloader.viewmodel.IToBePreparedViewModel
import tv.wfc.livestreamsales.application.model.Product

interface IProductOrderViewModel: IToBePreparedViewModel{
    val products: LiveData<List<Product>>
    val selectedProduct: LiveData<Product>
    val productsCount: LiveData<Int>
    fun prepareData(broadcastId: Long)
    fun selectProductByPosition(position: Int)
}