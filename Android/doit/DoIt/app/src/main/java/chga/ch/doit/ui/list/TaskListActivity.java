package chga.ch.doit.ui.list;

/**
 * Created by chsc on 20.04.17.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewTreeObserver;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import chga.ch.doit.DoItApplication;
import chga.ch.doit.R;
import chga.ch.doit.util.ActivityUtils;

public class TaskListActivity extends AppCompatActivity {

    public static final String PARAM_GROUP_NAME = "groupName";


    @Inject
    TaskListPresenter mPresenter;


    @InjectView(R.id.toolbar)
    Toolbar toolbar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_list);

        ButterKnife.inject(this);
        setSupportActionBar(toolbar);


        // Get the requested task id and set the toolbar title
        String groupName = getIntent().getStringExtra(PARAM_GROUP_NAME);
        getSupportActionBar().setTitle(groupName);

        TaskListFragment taskListFragment = (TaskListFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);

        if (taskListFragment == null) {
            taskListFragment = TaskListFragment.newInstance(groupName);

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    taskListFragment, R.id.contentFrame);
        }

        // Create the presenter
        DaggerTaskListComponent.builder()
                .tasksRepositoryComponent(((DoItApplication) getApplication())
                        .getTasksRepositoryComponent())
                .taskListPresenterModule(new TaskListPresenterModule(taskListFragment, groupName)).build()

                .inject(this);
    }

    @Override
    public void onBackPressed() {
        supportFinishAfterTransition();

    }
}
