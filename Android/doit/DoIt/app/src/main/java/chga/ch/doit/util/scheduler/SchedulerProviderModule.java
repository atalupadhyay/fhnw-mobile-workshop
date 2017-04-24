package chga.ch.doit.util.scheduler;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by chsc on 29.03.17.
 */

@Module
public class SchedulerProviderModule {

    @Singleton
    @Provides
    @Scheduler
    BaseSchedulerProvider provideSchedulerProvider(){
        return new SchedulerProvider();
    }

    @Singleton
    @Provides
    @ImmediateScheduler
    BaseSchedulerProvider provideImmediateSchedulerProvider(){
        return new ImmediateSchedulerProvider();
    }
}
