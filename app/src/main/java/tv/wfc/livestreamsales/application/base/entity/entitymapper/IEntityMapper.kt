package tv.wfc.livestreamsales.application.base.entity.entitymapper

interface IEntityMapper<FromT, ToT> {
    /**
     * Maps [FromT] instance to [ToT] instance.
     * @return instance of [ToT] type or null if mapping failed.
     */
    fun map(from: FromT): ToT?
}