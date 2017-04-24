package chga.ch.doit;

import android.app.Application;

import chga.ch.doit.data.DaggerTasksRepositoryComponent;
import chga.ch.doit.data.TasksRepositoryComponent;

/**
 * Created by chsc on 15.04.17.
 */

public class DoItApplication extends Application {

    private TasksRepositoryComponent repositoryComponent;

    public void onCreate(){
        super.onCreate();

        ApplicationComponent applicationComponent = DaggerApplicationComponent
                .builder()
                .applicationModule(new ApplicationModule(getApplicationContext()))
                .build();


        repositoryComponent = DaggerTasksRepositoryComponent
                .builder()
                .applicationComponent(applicationComponent)
                .build();
    }

    public TasksRepositoryComponent getTasksRepositoryComponent(){
        return repositoryComponent;
    }
}
