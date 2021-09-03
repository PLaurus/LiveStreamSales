package tv.wfc.livestreamsales.features.productorder.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.laurus.p.tools.livedata.LiveEvent
import com.laurus.p.tools.reactivex.NullablesWrapper
import io.reactivex.rxjava3.core.*
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.kotlin.Observables
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.subjects.BehaviorSubject
import tv.wfc.contentloader.model.ViewModelPreparationState
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.ComputationScheduler
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.MainThreadScheduler
import tv.wfc.livestreamsales.application.model.orders.OrderedProduct
import tv.wfc.livestreamsales.application.model.products.Product
import tv.wfc.livestreamsales.application.model.products.ProductGroup
import tv.wfc.livestreamsales.application.model.products.ProductVariant
import tv.wfc.livestreamsales.application.model.products.specification.Specification
import tv.wfc.livestreamsales.application.repository.products.IProductsRepository
import tv.wfc.livestreamsales.application.repository.productsorder.IProductsOrderRepository
import tv.wfc.livestreamsales.application.tools.errors.IApplicationErrorsLogger
import tv.wfc.livestreamsales.features.productorder.model.ProductBoxData
import tv.wfc.livestreamsales.features.productorder.model.SelectableSpecification
import javax.inject.Inject

class ProductOrderViewModel @Inject constructor(
    private val productsRepository: IProductsRepository,
    private val productsOrderRepository: IProductsOrderRepository,
    @MainThreadScheduler
    private val mainThreadScheduler: Scheduler,
    @ComputationScheduler
    private val computationScheduler: Scheduler,
    private val applicationErrorsLogger: IApplicationErrorsLogger
): ViewModel(), IProductOrderViewModel {
    private val disposables = CompositeDisposable()
    private val activeOperationsCount = BehaviorSubject.createDefault(0)

    private var orderProductsDisposable: Disposable? = null

    private val currentProductVariants = mutableSetOf<ProductVariant>()

    private val selectableSpecificationsByProductGroup = mutableMapOf<ProductGroup, MutableList<SelectableSpecification<*>>>()

    private val cartSubject = BehaviorSubject.create<List<OrderedProduct>>().apply{
        productsOrderRepository
            .getOrderedProductsFromCart()
            .toFlowable(BackpressureStrategy.LATEST)
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onNext = ::onNext,
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }

    private val selectedProductSubject = BehaviorSubject.create<NullablesWrapper<Product>>()

    private val selectedProductAmountSubject = BehaviorSubject.createDefault(NullablesWrapper<Int>(null)).apply{
        Observables
            .combineLatest(selectedProductSubject, cartSubject)
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onNext = { (product, cart) ->
                    val amount = product.value?.id?.let{ productId ->
                        cart?.firstOrNull { it.product.id == productId }?.amount ?: 0
                    }
                    onNext(NullablesWrapper(amount))
                },
                onError = {
                    onNext(NullablesWrapper(null))
                    applicationErrorsLogger::logError
                }
            )
            .addTo(disposables)
    }

    private var dataPreparationDisposable: Disposable? = null

    private var selectedProductGroup: ProductGroup? = null

    private lateinit var productGroups: List<ProductGroup>

    override val dataPreparationState = MutableLiveData<ViewModelPreparationState>(ViewModelPreparationState.DataIsNotPrepared)

    override val isAnyOperationInProgress = MutableLiveData(false).apply {
        activeOperationsCount
            .observeOn(mainThreadScheduler)
            .map { it > 0 }
            .subscribeBy(
                onNext = ::setValue,
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }

    override val productsCount = MutableLiveData<Int>()

    override val productBoxesData = MutableLiveData<List<ProductBoxData>>()

    override val currentProductGroupName = MutableLiveData<String>()

    override val currentProductGroupImageUrl = MutableLiveData<String?>()

    override val currentProductGroupSpecifications = MutableLiveData<List<Specification<*>>>()

    override val currentSelectableSpecifications = MutableLiveData<List<SelectableSpecification<*>>>()

    override val isProductSelected: LiveData<Boolean> = MutableLiveData(false).apply {
        selectedProductSubject
            .observeOn(mainThreadScheduler)
            .map { it.value != null }
            .distinctUntilChanged()
            .subscribeBy(
                onNext = ::setValue,
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }

    override val selectedProductPrice: LiveData<Float?> = MutableLiveData<Float?>().apply{
        selectedProductSubject
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onNext = { value = it.value?.price },
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }

    override val selectedProductOldPrice: LiveData<Float?> = MutableLiveData<Float?>().apply{
        selectedProductSubject
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onNext = { value = it.value?.oldPrice },
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }

    override val selectedProductAmount: LiveData<Int?> = MutableLiveData<Int?>().apply{
        selectedProductAmountSubject
            .distinctUntilChanged()
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onNext = { value = it.value },
                onError = {
                    value = null
                    applicationErrorsLogger.logError(it)
                }
            )
            .addTo(disposables)
    }

    override val isIncreasingOfSelectedProductAmountAllowed: LiveData<Boolean> = MutableLiveData(false).apply{
        Observables.combineLatest(selectedProductSubject, selectedProductAmountSubject)
            .map { (nullableProduct, nullableAmount) ->
                nullableProduct.value?.quantityInStock?.let { maxAvailableAmount ->
                    (nullableAmount.value ?: 0) < maxAvailableAmount
                } ?: false
            }
            .distinctUntilChanged()
            .subscribeOn(computationScheduler)
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onNext = ::setValue,
                onError = {
                    value = false
                    applicationErrorsLogger::logError
                }
            )
            .addTo(disposables)
    }

    override val cart: LiveData<List<OrderedProduct>> = MutableLiveData<List<OrderedProduct>>(emptyList()).apply{
        cartSubject
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onNext = ::setValue,
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }

    override val orderedProductsFinalPrice: LiveData<Float> = MutableLiveData(0f).apply{
        cartSubject
            .observeOn(mainThreadScheduler)
            .map { it.calculatePrice() }
            .subscribeBy(
                onNext = ::setValue,
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }

    override val areProductsOrderedEvent = LiveEvent<Unit>()

    @Synchronized
    override fun prepareData(broadcastId: Long){
        if(dataPreparationState.value != ViewModelPreparationState.DataIsNotPrepared &&
            dataPreparationState.value != null) return

        dataPreparationDisposable?.dispose()

        dataPreparationDisposable = Completable
            .mergeArray(
                prepareProductsInformation(broadcastId),
                prepareCart()
            )
            .observeOn(mainThreadScheduler)
            .doOnSubscribe {
                dataPreparationState.value = ViewModelPreparationState.DataIsBeingPrepared
            }
            .subscribeBy(
                onComplete = {
                    dataPreparationState.value = ViewModelPreparationState.DataIsPrepared
                },
                onError = {
                    dataPreparationState.value = ViewModelPreparationState.FailedToPrepareData()
                    applicationErrorsLogger.logError(it)
                }
            )
            .addTo(disposables)

    }

    @Synchronized
    override fun selectProductGroupByPosition(position: Int) {
        val selectedProduct = productGroups.getOrNull(position) ?: return

        selectProductGroup(selectedProduct)
    }

    @Synchronized
    override fun filter(specificationPosition: Int, valuePosition: Int) {
        val selectedProductGroup = this.selectedProductGroup ?: return
        val selectableSpecifications = selectableSpecificationsByProductGroup[selectedProductGroup] ?: return

        if(selectedProductSubject.value!!.value != null) deselectProduct()

        for(i in selectableSpecifications.lastIndex downTo specificationPosition + 1){
            selectableSpecifications.removeAt(i)
        }

        val selectableSpecification = selectableSpecifications
            .elementAtOrNull(specificationPosition)
            ?.apply { selectValue(valuePosition) } ?: return

        if(selectableSpecification.selectedValue != null){
            val filteredProductVariants = currentProductVariants.filter(selectableSpecifications)

            val nextSelectableSpecification = createNextSelectableSpecification(
                selectableSpecifications,
                filteredProductVariants
            )

            if(nextSelectableSpecification == null){
                val filteredProductVariant = filteredProductVariants.firstOrNull()
                if(filteredProductVariant == null){
                    deselectProduct()
                } else{
                    Product.create(selectedProductGroup, filteredProductVariant.id)?.let{
                        selectProduct(it)
                    }
                }
            } else{
                selectableSpecifications.add(nextSelectableSpecification)
            }
        }

        this.currentSelectableSpecifications.value = selectableSpecifications
    }

    @JvmName("filterProductVariantBySelectableSpecifications")
    private fun Collection<ProductVariant>.filter(
        selectableSpecifications: Collection<SelectableSpecification<*>>
    ): List<ProductVariant>{
        val selectedSpecifications = selectableSpecifications.toSpecifications()

        return filter(selectedSpecifications)
    }

    @JvmName("filterProductVariantBySpecifications")
    private fun Collection<ProductVariant>.filter(
        specifications: Collection<Specification<*>>
    ): List<ProductVariant>{
        return filter filterProductVariants@{ productVariant ->
            specifications.forEach { selectedSpecification ->
                if(!productVariant.specifications.contains(selectedSpecification)) return@filterProductVariants false
            }

            return@filterProductVariants true
        }
    }

    @Synchronized
    override fun decreaseSelectedProductAmount() {
        val product = selectedProductSubject.value?.value ?: return
        removeProductFromCart(product)
    }

    @Synchronized
    override fun increaseSelectedProductAmount() {
        val product = selectedProductSubject.value?.value ?: return
        addProductToCart(product)
    }

    @Synchronized
    override fun deleteProductFromCart(productId: Long) {
        productsOrderRepository
            .removeAllProductUnitsFromCart(productId)
            .observeOn(mainThreadScheduler)
            .doOnSubscribe { incrementActiveOperationsCount() }
            .doOnTerminate { decrementActiveOperationsCount() }
            .subscribeBy(applicationErrorsLogger::logError)
            .addTo(disposables)
    }

    override fun orderProducts() {
        orderProductsDisposable?.dispose()

        orderProductsDisposable = productsOrderRepository
            .orderProductsFromCart()
            .observeOn(mainThreadScheduler)
            .doOnSubscribe { incrementActiveOperationsCount() }
            .doOnTerminate(::decrementActiveOperationsCount)
            .subscribeBy(
                onComplete = { areProductsOrderedEvent.value = Unit },
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }

    private fun selectProduct(product: Product){
        selectedProductSubject.onNext(NullablesWrapper(product))
    }

    private fun deselectProduct(){
        selectedProductSubject.onNext(NullablesWrapper(null))
    }

    private fun prepareProductsInformation(broadcastId: Long): Completable{
        return getProductGroupsFromRemote(broadcastId)
            .observeOn(mainThreadScheduler)
            .flatMapCompletable { products ->
                Completable.mergeArray(
                    prepareProductsData(products)
                )
            }
    }

    private fun prepareCart(): Completable{
        return cartSubject
            .observeOn(mainThreadScheduler)
            .firstOrError()
            .ignoreElement()
    }

    private fun getProductGroupsFromRemote(broadcastId: Long): Single<List<ProductGroup>>{
        return productsRepository
            .getProductGroups(broadcastId)
            .lastOrError()
            .filter{ it.isNotEmpty() }
            .toSingle()
    }

    private fun prepareProductsData(productGroups: List<ProductGroup>): Completable{
        return Completable.fromRunnable {
            updateProductGroups(productGroups)
        }
    }

    @Synchronized
    private fun updateProductGroups(newProductGroups: List<ProductGroup>){
        this.productGroups = newProductGroups
        productsCount.value = productGroups.size
        updateProductBoxesData(productGroups)
        val groupToSelect = productGroups.getOrNull(0) ?: return
        selectProductGroup(groupToSelect)
    }

    @Synchronized
    private fun selectProductGroup(newProductGroup: ProductGroup){
        this.selectedProductGroup = newProductGroup

        currentProductGroupName.value = newProductGroup.name
        currentProductGroupImageUrl.value = newProductGroup.image
        currentProductGroupSpecifications.value = newProductGroup.specifications

        deselectProduct()
        updateCurrentProductVariants(newProductGroup)
        changeSelectableSpecifications(newProductGroup)
    }

    @Synchronized
    private fun updateCurrentProductVariants(productGroup: ProductGroup){
        val newProductVariants = productGroup.productVariants

        currentProductVariants.clear()
        currentProductVariants.addAll(newProductVariants)
    }

    private fun updateProductBoxesData(productGroups: List<ProductGroup>){
        val productBoxesData = mutableListOf<ProductBoxData>()

        productGroups.forEachIndexed { productPosition, product ->
            val productImageUrl = product.image
            val productCountInStock = product.countInStock()

            productBoxesData.add(
                ProductBoxData(
                    productPosition,
                    productImageUrl,
                    productCountInStock
                )
            )
        }

        this.productBoxesData.value = productBoxesData
    }

    @Synchronized
    private fun changeSelectableSpecifications(productGroup: ProductGroup){
        val doesSelectableSpecificationsExist = selectableSpecificationsByProductGroup.containsKey(productGroup)

        if(!doesSelectableSpecificationsExist){
            val newSelectableSpecifications = mutableListOf<SelectableSpecification<*>>()
            selectableSpecificationsByProductGroup[productGroup] = newSelectableSpecifications

            createNextSelectableSpecification(
                newSelectableSpecifications,
                productGroup.productVariants
            )?.let{ nextSelectableSpecification ->
                addSelectableSpecification(productGroup, nextSelectableSpecification)
            }
        }

        val selectableSpecifications = selectableSpecificationsByProductGroup[productGroup] ?: return

        val specifications = selectableSpecifications.toSpecifications()

        val filteredProductVariants = productGroup.productVariants.filter(specifications)

        if(selectableSpecifications.none { it.selectedValue == null } &&
            createNextSelectableSpecification(selectableSpecifications, filteredProductVariants) == null){
            filteredProductVariants.getOrNull(0)?.let{ selectedProductVariant ->
                Product.create(productGroup, selectedProductVariant.id)?.let{ selectedProduct ->
                    selectProduct(selectedProduct)
                }
            }
        }

        this.currentSelectableSpecifications.value = selectableSpecifications
    }

    private fun ProductGroup.countInStock(): Int{
        return productVariants
            .map{ it.quantityInStock }
            .reduce{ acc, next -> acc + next}
    }

    // region Products in cart

    private fun addProductToCart(
        product: Product,
        amount: Int = 1
    ) {
        productsOrderRepository
            .addProductToCart(product, amount)
            .observeOn(mainThreadScheduler)
            .doOnSubscribe { incrementActiveOperationsCount() }
            .doOnTerminate { decrementActiveOperationsCount() }
            .subscribeBy(applicationErrorsLogger::logError)
            .addTo(disposables)
    }

    private fun List<OrderedProduct>.calculatePrice(): Float{
        return map { it.product.price * it.amount }
            .reduceOrNull { sum, nextPrice -> sum + nextPrice } ?: 0f
    }

    private fun removeProductFromCart(
        product: Product,
        amount: Int = 1
    ){
        productsOrderRepository
            .removeProductFromCart(product, amount)
            .observeOn(mainThreadScheduler)
            .doOnSubscribe { incrementActiveOperationsCount() }
            .doOnTerminate { decrementActiveOperationsCount() }
            .subscribeBy(applicationErrorsLogger::logError)
            .addTo(disposables)
    }

    // endregion

    // region Selectable specifications

    private fun createNextSelectableSpecification(
        selectableSpecifications: Collection<SelectableSpecification<*>>,
        filteredProductVariants: Collection<ProductVariant>
    ): SelectableSpecification<*>?{
        val lastSelectableSpecificationIndex = selectableSpecifications.size.minus(1)
        val nextSelectableSpecificationIndex = lastSelectableSpecificationIndex + 1

        if(filteredProductVariants.isEmpty()) return null

        val nextSpecification = filteredProductVariants
            .elementAt(0)
            .specifications
            .getOrNull(nextSelectableSpecificationIndex) ?: return null

        val nextSpecificationClass = nextSpecification::class
        val nextSelectableSpecificationName = nextSpecification.name

        val nextFilterValues = mutableSetOf<Any>()

        for(productVariant in filteredProductVariants) {
            val selectableSpecificationValue = productVariant
                .specifications
                .firstOrNull { nextSpecificationClass.isInstance(it) && it.name == nextSelectableSpecificationName }
                ?.value ?: continue

            nextFilterValues.add(selectableSpecificationValue)
        }

        return when(nextSpecification){
            is Specification.ColorSpecification -> {
                val availableValues = nextFilterValues.map{ it as Int }.toSet()
                SelectableSpecification.ColorSpecification(
                    nextSelectableSpecificationName,
                    availableValues
                )
            }
            is Specification.DescriptiveSpecification -> {
                val availableValues = nextFilterValues.map{ it as String }.toSet()
                SelectableSpecification.DescriptiveSpecification(
                    nextSelectableSpecificationName,
                    availableValues
                )
            }
        }
    }

    @Synchronized
    private fun addSelectableSpecification(
        productGroup: ProductGroup,
        selectableSpecification: SelectableSpecification<*>
    ){
        val selectableSpecifications = selectableSpecificationsByProductGroup[productGroup]

        if(selectableSpecifications != null){
            selectableSpecifications.add(selectableSpecification)
        } else{
            val newSelectableSpecifications = mutableListOf(selectableSpecification)
            selectableSpecificationsByProductGroup[productGroup] = newSelectableSpecifications
        }

        if(selectedProductGroup == productGroup){
            this.currentSelectableSpecifications.value = selectableSpecifications
        }
    }

    private fun clearSelectableSpecifications(productGroup: ProductGroup){
        val selectableSpecifications = selectableSpecificationsByProductGroup[productGroup]

        if(selectableSpecifications != null){
            selectableSpecificationsByProductGroup.remove(productGroup)
        }

        this.currentSelectableSpecifications.value = emptyList()
    }

    private fun SelectableSpecification<*>.toSpecification(): Specification<*>?{
        return when(this){
            is SelectableSpecification.ColorSpecification -> {
                Specification.ColorSpecification(
                    name = name,
                    color = selectedValue ?: return null,
                    colorName = selectedColorName ?: return null
                )
            }
            is SelectableSpecification.DescriptiveSpecification -> {
                Specification.DescriptiveSpecification(name, selectedValue ?: return null)
            }
        }
    }

    private fun Collection<SelectableSpecification<*>>.toSpecifications(): Set<Specification<*>>{
        val specifications = mutableSetOf<Specification<*>>()

        forEach { selectableSpecification ->
            selectableSpecification.toSpecification()?.let {
                specifications.add(it)
            }
        }

        return specifications
    }

    // endregion

    @Synchronized
    private fun incrementActiveOperationsCount(){
        val currentActiveOperationsCount = activeOperationsCount.value ?: 0
        val newActiveOperationsCount = currentActiveOperationsCount + 1

        activeOperationsCount.onNext(newActiveOperationsCount)
    }

    @Synchronized
    private fun decrementActiveOperationsCount(){
        val currentActiveOperationsCount = activeOperationsCount.value ?: 0
        val newActiveOperationsCount = currentActiveOperationsCount - 1

        activeOperationsCount.onNext(newActiveOperationsCount)
    }
}