package tv.wfc.livestreamsales.application.di.modules.database

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import tv.wfc.livestreamsales.application.di.modules.database.qualifiers.ApplicationDatabaseName
import tv.wfc.livestreamsales.application.di.scope.ApplicationScope
import tv.wfc.livestreamsales.application.database.AppDatabase
import tv.wfc.livestreamsales.application.database.tables.CartDao

@Module
class DatabaseModule {
    @ApplicationScope
    @Provides
    @ApplicationDatabaseName
    internal fun provideApplicationDataBaseName() = "application_database"

    @ApplicationScope
    @Provides
    internal fun provideAppDatabase(
        applicationContext: Context,
        @ApplicationDatabaseName
        applicationDatabaseName: String
    ): AppDatabase {
        return Room
            .databaseBuilder(
                applicationContext,
                AppDatabase::class.java,
                applicationDatabaseName
            )
            .build()
    }

    @Provides
    internal fun provideOrderedProductsTable(
        applicationDatabase: AppDatabase
    ): CartDao{
        return applicationDatabase.cartDao()
    }
}