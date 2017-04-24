package chga.ch.doit.data;

import android.content.Context;

import javax.inject.Singleton;

import chga.ch.doit.data.local.TasksLocalDataSource;
import chga.ch.doit.util.scheduler.BaseSchedulerProvider;
import chga.ch.doit.util.scheduler.Scheduler;
import dagger.Module;
import dagger.Provides;

/**
 * Created by chsc on 15.04.17.
 */

@Module
public class TasksRepositoryModule {

    @Singleton
    @Provides
    @Local
    TasksDataSource provideLocalTasksDataSource(Context context, @Scheduler BaseSchedulerProvider baseSchedulerProvider){
        return new TasksLocalDataSource(context,baseSchedulerProvider);
    }
}
