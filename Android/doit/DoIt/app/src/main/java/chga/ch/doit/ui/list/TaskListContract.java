package chga.ch.doit.ui.list;

import java.util.List;

import chga.ch.doit.BasePresenter;
import chga.ch.doit.BaseView;
import chga.ch.doit.model.Task;


/**
 * Created by chsc on 20.04.17.
 */

public interface TaskListContract {

    interface View extends BaseView<Presenter> {

        void showTasks(List<Task> tasks);

        void setLoadingIndicator(boolean active);

        void openTask(String taskId);

    }

    interface Presenter extends BasePresenter{

        void loadTasks(boolean forceUpdate);

        void openTask(String taskId);

        void completeTask(String taskId);

        void deleteTask(String taskId);
    }
}
