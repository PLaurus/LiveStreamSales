package tv.wfc.livestreamsales.application.base.entity.entitymapper

import tv.wfc.livestreamsales.application.base.entity.domain.IDomainEntity

interface IEntityMapper<ExternalEntity, DomainEntity: IDomainEntity> {
    /**
     * Maps [ExternalEntity] to [DomainEntity] entity.
     * @return Domain entity instance or null if mapping failed.
     */
    fun mapToDomainEntity(from: ExternalEntity): DomainEntity?

    /**
     * Maps [DomainEntity] entity to [ExternalEntity].
     * @return [DomainEntity] instance or null if mapping failed.
     */
    fun mapToExternalEntity(from: DomainEntity): ExternalEntity?
}