package chga.ch.doit.ui.overview;

import android.support.annotation.NonNull;

import java.util.List;
import java.util.Map;

import chga.ch.doit.BasePresenter;
import chga.ch.doit.BaseView;
import chga.ch.doit.model.Task;

/**
 * Created by chsc on 15.04.17.
 */

public class OverviewContract {

    interface View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean active);

        void showTasks(List<Task> tasks);

        void showAddTask();

        void openTaskGroup(String groupName);

        void showLoadingTasksError();

        void showNoTasks();

        boolean isActive();

    }

    interface Presenter extends BasePresenter {

        void loadTasks(boolean forceUpdate);

        void addNewTask();

        void openTaskGroup(String groupName);

    }
}
