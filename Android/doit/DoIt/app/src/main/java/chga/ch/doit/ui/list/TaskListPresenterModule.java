package chga.ch.doit.ui.list;

import android.support.annotation.Nullable;

import dagger.Module;
import dagger.Provides;

/**
 * Created by chsc on 20.04.17.
 */

@Module
public class TaskListPresenterModule {

    private final TaskListContract.View mView;

    private final String mGroupName;

    public TaskListPresenterModule(TaskListContract.View view, String groupName) {
        mView = view;
        mGroupName = groupName;
    }

    @Provides
    TaskListContract.View provideTaskListContractView() {
        return mView;
    }

    @Provides
    String provideGroupName() {
        return mGroupName;
    }
}
