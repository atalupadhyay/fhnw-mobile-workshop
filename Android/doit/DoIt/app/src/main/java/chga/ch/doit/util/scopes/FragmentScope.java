package chga.ch.doit.util.scopes;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by chsc on 25.03.17.
 *
 *
 * In dagger, an unscoped component cannot depend on an scoped component.
 * As  {@link chga.ch.doit.data.TasksRepositoryComponent} is a scoped {@link dagger.Component}
 * {@link javax.inject.Singleton}, we create a custom scope to be used by all fragment components.
 * Additionally, a component with a specific scope cannot have a sub component with the same scope.
 *
 */

@Documented
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface FragmentScope {
}
