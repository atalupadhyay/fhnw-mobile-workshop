package chga.ch.doit.ui.list;

import chga.ch.doit.data.TasksRepositoryComponent;
import chga.ch.doit.util.scopes.FragmentScope;
import dagger.Component;

/**
 * Created by chsc on 20.04.17.
 */

/**
 * This is a Dagger component. Refer to {@link chga.ch.doit.DoItApplication} for the list of Dagger components
 * used in this application.
 * <P>
 * Because this component depends on the {@link TasksRepositoryComponent}, which is a singleton, a
 * scope must be specified. All fragment components use a custom scope for this purpose.
 */
@FragmentScope
@Component(dependencies = TasksRepositoryComponent.class, modules = TaskListPresenterModule.class)
public interface TaskListComponent {

    void inject(TaskListActivity activity);
}
