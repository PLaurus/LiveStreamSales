package tv.wfc.core.entity

interface IEntityMapper<FromT, ToT> {
    /**
     * Maps [FromT] instance to [ToT] instance.
     * @return instance of [ToT] type or null if mapping failed.
     */
    fun map(from: FromT): ToT?
}