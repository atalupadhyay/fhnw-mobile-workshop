package chga.ch.doit.data;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;

/**
 * Created by chsc on 15.04.17.
 *
 * Qualifier that determines the datasource {@link TasksDataSource}. Currently we only have one
 * local datasource, if we wish to add a another {@link TasksDataSource} type, or example a remote
 * datasource, we have to generate a new qualifier.
 */

@Qualifier
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface Local {

}
