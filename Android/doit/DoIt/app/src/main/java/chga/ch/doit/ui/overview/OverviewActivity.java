package chga.ch.doit.ui.overview;

/**
 * Created by chsc on 15.04.17.
 */

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import javax.inject.Inject;

import butterknife.ButterKnife;
import chga.ch.doit.DoItApplication;
import chga.ch.doit.R;
import chga.ch.doit.util.ActivityUtils;

public class OverviewActivity extends AppCompatActivity {

    @Inject
    OverviewPresenter mTasksPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);
        ButterKnife.inject(this);

        // Set up the toolbar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.task_overview_title);

        OverviewFragment overviewFragment =
                (OverviewFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (overviewFragment == null) {
            // Create the fragment
            overviewFragment = OverviewFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), overviewFragment, R.id.contentFrame);
        }

        // Create the presenter
        DaggerOverviewComponent.builder()
                .tasksRepositoryComponent(((DoItApplication) getApplication()).getTasksRepositoryComponent())
                .overviewPresenterModule(new OverviewPresenterModule(overviewFragment)).build()
                .inject(this);

    }



}
