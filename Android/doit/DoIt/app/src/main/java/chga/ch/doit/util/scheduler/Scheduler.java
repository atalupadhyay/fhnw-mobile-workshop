package chga.ch.doit.util.scheduler;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;

/**
 * Created by chsc on 25.03.17.
 *
 *
 * An identifier qualifier to tell Dagger which interface should be provided in the corresponding
 * factories. See {@link }
 */

@Qualifier
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface Scheduler {
}
