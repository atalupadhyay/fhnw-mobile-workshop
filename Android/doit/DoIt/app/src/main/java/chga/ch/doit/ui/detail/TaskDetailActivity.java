package chga.ch.doit.ui.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import chga.ch.doit.DoItApplication;
import chga.ch.doit.R;
import chga.ch.doit.ui.overview.OverviewPresenter;
import chga.ch.doit.util.ActivityUtils;

/**
 * Created by chsc on 15.04.17.
 */



public class TaskDetailActivity extends AppCompatActivity {

    public static final String PARAM_TASK_ID = "taskId";

    public static final int REQUEST_ADD_TASK = 1;

    @InjectView(R.id.toolbar)
    Toolbar toolbar;

    @Inject
    TaskDetailPresenter mTaskDetailPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);

        ButterKnife.inject(this);
        setSupportActionBar(toolbar);

        // Get the requested task id
        String taskId = getIntent().getStringExtra(PARAM_TASK_ID);

        getSupportActionBar().setTitle(taskId == null || taskId.isEmpty() ? getString(R.string.task_detail_create_task_title) : getString(R.string.task_detail_update_task_title));
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TaskDetailFragment taskTaskDetailFragment = (TaskDetailFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);

        if (taskTaskDetailFragment == null) {
            taskTaskDetailFragment = TaskDetailFragment.newInstance(taskId);

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    taskTaskDetailFragment, R.id.contentFrame);
        }

        // Create the presenter
        DaggerTaskDetailComponent.builder()
                .tasksRepositoryComponent(((DoItApplication) getApplication())
                        .getTasksRepositoryComponent())
                .taskDetailPresenterModule(new TaskDetailPresenterModule(taskTaskDetailFragment, taskId)).build()

                .inject(this);


    }
}
