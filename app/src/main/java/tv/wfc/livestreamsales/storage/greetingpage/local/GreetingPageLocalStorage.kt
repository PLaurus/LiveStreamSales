package tv.wfc.livestreamsales.storage.greetingpage.local

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import tv.wfc.livestreamsales.R
import tv.wfc.livestreamsales.di.components.app.modules.reactivex.qualifiers.IoScheduler
import tv.wfc.livestreamsales.model.application.greetingpage.GreetingPage
import tv.wfc.livestreamsales.storage.greetingpage.IGreetingPageStorage
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class GreetingPageLocalStorage @Inject constructor(
    @IoScheduler
    private val ioScheduler: Scheduler,
    private val context: Context
): IGreetingPageStorage {

    private val greetingPages = setOf(*createDefaultGreetingPages().toTypedArray())

    override fun getGreetingPages(): Single<Set<GreetingPage>> {
        return Single
            .just(greetingPages.toSet())
            .subscribeOn(ioScheduler)
    }

    override fun getGreetingPage(index: Int): Single<GreetingPage> {
        return Single
            .fromCallable { greetingPages.elementAt(index) }
            .subscribeOn(ioScheduler)
    }

    private fun createDefaultGreetingPages(): Set<GreetingPage>{
        return setOf(
                createDefaultGreetingPage1(),
                createDefaultGreetingPage2(),
                createDefaultGreetingPage3(),
                createDefaultGreetingPage4()
            )
            .sortedBy {
                it.pageOrder
            }
            .toSet()
    }

    private fun createDefaultGreetingPage1(): GreetingPage{
        return createDefaultGreetingPage(
            1,
            R.drawable.greeting_1,
            R.string.greeting_fragment_page1_title,
            R.string.greeting_fragment_page1_description
        )
    }

    private fun createDefaultGreetingPage2(): GreetingPage{
        return createDefaultGreetingPage(
            2,
            R.drawable.greeting_2,
            R.string.greeting_fragment_page2_title,
            R.string.greeting_fragment_page2_description
        )
    }

    private fun createDefaultGreetingPage3(): GreetingPage{
        return createDefaultGreetingPage(
            3,
            R.drawable.greeting_3,
            R.string.greeting_fragment_page3_title,
            R.string.greeting_fragment_page3_description
        )
    }

    private fun createDefaultGreetingPage4(): GreetingPage{
        return createDefaultGreetingPage(
            4,
            R.drawable.greeting_4,
            R.string.greeting_fragment_page4_title,
            R.string.greeting_fragment_page4_description
        )
    }

    private fun createDefaultGreetingPage(
        pageOrder: Int,
        @DrawableRes
        imageResId: Int,
        @StringRes
        titleResId: Int,
        @StringRes
        descriptionResId: Int
    ): GreetingPage{
        val greetingPageImage = ContextCompat.getDrawable(context, imageResId)
        val greetingPageTitle = context.resources.getString(titleResId)
        val greetingPageDescription = context.resources.getString(descriptionResId)

        return GreetingPage(
            pageOrder = pageOrder,
            image = greetingPageImage,
            title = greetingPageTitle,
            description = greetingPageDescription
        )
    }
}