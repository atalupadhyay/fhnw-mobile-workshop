package chga.ch.doit.data;

/**
 * Created by chsc on 15.04.17.
 */

import javax.inject.Singleton;

import chga.ch.doit.ApplicationComponent;
import chga.ch.doit.util.scheduler.BaseSchedulerProvider;
import chga.ch.doit.util.scheduler.Scheduler;
import chga.ch.doit.util.scheduler.SchedulerProviderModule;
import dagger.Component;

/**
 * This is a Dagger component. Refer to {@link chga.ch.doit.DoItApplication} for the list of Dagger components
 * used in this application.
 * <P>
 * Even though Dagger allows annotating a {@link Component @Component} as a singleton, the code
 * itself must ensure only one instance of the class is created. This is done in {@link
 * chga.ch.doit.DoItApplication}.
 */
@Singleton
@Component(modules = {SchedulerProviderModule.class,TasksRepositoryModule.class},dependencies = ApplicationComponent.class)
public interface TasksRepositoryComponent {

    TasksRepository getTasksRepository();

    @Scheduler
    BaseSchedulerProvider getBaseSchedulerProvider();
}
