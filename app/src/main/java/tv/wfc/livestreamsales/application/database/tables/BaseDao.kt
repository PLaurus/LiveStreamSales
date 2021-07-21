package tv.wfc.livestreamsales.application.database.tables

import androidx.room.*
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

abstract class BaseDao<T> {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insert(entity: T): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun rxInsert(entity: T): Single<Long>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insert(entities: List<T>): List<Long>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun rxInsert(entities: List<T>): Single<List<Long>>

    @Update
    abstract fun update(entity: T)

    @Update
    abstract fun rxUpdate(entity: T): Completable

    @Update
    abstract fun update(entities: List<T>)

    @Update
    abstract fun rxUpdate(entities: List<T>): Completable

    @Transaction
    open fun insertOrUpdate(entity: T) {
        val id = insert(entity)
        if (id == -1L) update(entity)
    }

    fun rxInsertOrUpdate(entity: T): Completable{
        return Completable.fromRunnable {
            insertOrUpdate(entity)
        }
    }

    @Transaction
    open fun insertOrUpdate(entities: List<T>) {
        val insertResult = insert(entities)
        val updateList = mutableListOf<T>()

        for (i in insertResult.indices) {
            if (insertResult[i] == -1L) updateList.add(entities[i])
        }

        if (updateList.isNotEmpty()) update(updateList)
    }

    fun rxInsertOrUpdate(entities: List<T>): Completable{
        return Completable.fromRunnable {
            insertOrUpdate(entities)
        }
    }

    @Delete
    abstract fun delete(entity: T)

    @Delete
    abstract fun rxDelete(entity: T): Completable

    @Delete
    abstract fun delete(entities: List<T>)

    @Delete
    abstract fun rxDelete(entities: List<T>): Completable
}