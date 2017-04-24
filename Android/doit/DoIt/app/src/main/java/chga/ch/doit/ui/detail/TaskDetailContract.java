package chga.ch.doit.ui.detail;

import java.util.Date;
import java.util.List;

import chga.ch.doit.BasePresenter;
import chga.ch.doit.BaseView;

/**
 * Created by chsc on 15.04.17.
 */

public interface TaskDetailContract {

    interface View extends BaseView<Presenter>{

        void showTask(String title, String description, String group,Date date);

        void showTasksList();

        void showGroups(List<String> groups);


    }

    interface Presenter extends BasePresenter{

        void saveTask(String title, String description,String group, Date date);

        void loadGroups();

    }
}
