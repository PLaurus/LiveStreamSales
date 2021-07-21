package tv.wfc.livestreamsales.application.database

import androidx.room.Database
import androidx.room.RoomDatabase
import tv.wfc.livestreamsales.application.database.tables.CartDao
import tv.wfc.livestreamsales.application.model.orders.OrderedProduct

@Database(
    entities = [
        OrderedProduct::class
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase(){
    internal abstract fun cartDao(): CartDao
}