package tv.wfc.livestreamsales.application.di.modules.viewmodelprovider.mapkeys;

import androidx.lifecycle.ViewModel;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import dagger.MapKey;

// MUST BE WRITTEN IN JAVA!
// Kotlin causes a dagger error: Missing binding for Map<KClass<out ViewModel>,Provider<ViewModel>>
// WHEN PROVIDING MAP IN KOTLIN FILE YOU MUST ADD @JvmSuppressWildcards FOR VALUE OF THE MAP!
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@MapKey
public @interface ViewModelKey {
    Class<? extends ViewModel> value();
}
