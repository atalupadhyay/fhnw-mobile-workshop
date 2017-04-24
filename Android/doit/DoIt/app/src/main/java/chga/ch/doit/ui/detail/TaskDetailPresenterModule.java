package chga.ch.doit.ui.detail;

import android.support.annotation.Nullable;

import dagger.Module;
import dagger.Provides;

/**
 * Created by chsc on 15.04.17.
 */

/**
 * This is a Dagger module. We use this to pass in the View dependency to the
 * {@link TaskDetailPresenter}.
 */
@Module
public class TaskDetailPresenterModule {

    private final TaskDetailContract.View mView;

    private final String mTaskId;

    public TaskDetailPresenterModule(TaskDetailContract.View view, String taskId) {
        mView = view;
        mTaskId = taskId;
    }

    @Provides
    TaskDetailContract.View provideTaskDetailContractView() {
        return mView;
    }

    @Provides
    @Nullable
    String provideTaskId() {
        return mTaskId;
    }
}
