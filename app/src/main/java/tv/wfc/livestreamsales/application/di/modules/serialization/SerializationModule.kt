package tv.wfc.livestreamsales.application.di.modules.serialization

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Binds
import dagger.Module
import dagger.Provides
import tv.wfc.livestreamsales.application.tools.serialization.GsonSerializationBehavior
import tv.wfc.livestreamsales.application.tools.serialization.ISerializationBehavior

@Module
abstract class SerializationModule {
    @Binds
    internal abstract fun bindISerializationBehavior(
        serializationBehavior: GsonSerializationBehavior
    ): ISerializationBehavior

    companion object {
        @Provides
        internal fun provideGson(): Gson {
            return GsonBuilder()
                .setPrettyPrinting()
                .create()
        }
    }
}