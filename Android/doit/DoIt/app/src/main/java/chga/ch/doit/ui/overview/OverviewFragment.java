package chga.ch.doit.ui.overview;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.common.reflect.ImmutableTypeToInstanceMap;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import butterknife.InjectView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import chga.ch.doit.R;
import chga.ch.doit.model.Task;
import chga.ch.doit.ui.detail.TaskDetailActivity;
import chga.ch.doit.ui.detail.TaskDetailContract;
import chga.ch.doit.ui.list.TaskListActivity;
import chga.ch.doit.ui.list.TaskListAdapter;
import chga.ch.doit.util.ItemClickSupport;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * Created by chsc on 15.04.17.
 */

public class OverviewFragment extends Fragment implements OverviewContract.View {


    OverviewContract.Presenter mPresenter;

    private OverviewAdapter mOverviewAdapter;

    @InjectView(R.id.noTasks)
    View mNoTasksView;

    @InjectView(R.id.noTasksIcon)
    ImageView mNoTaskIcon;

    @InjectView(R.id.noTasksMain)
    TextView mNoTaskMainView;


    @InjectView(R.id.tasksLL)
    LinearLayout mTasksView;

    @InjectView(R.id.tasks_list)
    RecyclerView mTaskList;

    TextView currentDate;
    PieChart overallProgess;
    AppBarLayout appBarLayout;
    TextView overallProgessText;


    public OverviewFragment() {
        // Requires empty public constructor
    }

    public static OverviewFragment newInstance() {
        return new OverviewFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mOverviewAdapter = new OverviewAdapter(getActivity());
    }


    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_overview, container, false);
        ButterKnife.inject(this,view);

        // Set up floating action button
        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab_add_task);
        fab.setImageResource(R.drawable.ic_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.addNewTask();
            }
        });
        // Set up progress indicator
        final ScrollChildSwipeRefreshLayout swipeRefreshLayout =
                (ScrollChildSwipeRefreshLayout) view.findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark)
        );
        // Set the scrolling view in the custom SwipeRefreshLayout.
        swipeRefreshLayout.setScrollUpChild(mTaskList);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.loadTasks(true);
            }
        });


        currentDate = (TextView) getActivity().findViewById(R.id.date);
        overallProgess = (PieChart) getActivity().findViewById(R.id.piechart);
        appBarLayout = (AppBarLayout) getActivity().findViewById(R.id.appbar);
        overallProgessText = (TextView) getActivity().findViewById(R.id.itemcount_textview);

        return view;
    }


    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Setup the recyclerview
        mTaskList.setAdapter(mOverviewAdapter);
        mTaskList.setLayoutManager(new GridLayoutManager(getActivity(),2));
        setOverallProgess(new ArrayList<Task>());

    }

    @Override
    public void onResume() {
        super.onResume();
        ItemClickSupport.addTo(mTaskList).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {

                Intent intent = new Intent(getActivity(), TaskListActivity.class);
                intent.putExtra(TaskListActivity.PARAM_GROUP_NAME, mOverviewAdapter.getGroupTitleForPosition(position));
                View pieChart = v.findViewById(R.id.piechart);
                Pair<View,String> piechartTransition = Pair.create(pieChart,"piechart");
                Pair<View,String> backgroundTranistion = Pair.create(v,"task_list_background");
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation((Activity) getActivity(), backgroundTranistion,piechartTransition);
                getActivity().startActivity(intent, options.toBundle());
            }
        });
        mPresenter.subscribe();
        setCurrentDate();

    }

    private void setCurrentDate(){
        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy");
        String date = df.format(Calendar.getInstance().getTime());
        currentDate.setText(date);
    }
    
    private void setOverallProgess(List<Task> tasks){
        overallProgess.setDrawHoleEnabled(true);
        overallProgess.setHoleColor(Color.TRANSPARENT);
        overallProgess.getDescription().setEnabled(false);
        overallProgess.setDrawEntryLabels(true);
        overallProgess.setCenterTextColor(Color.WHITE);
        overallProgess.setEntryLabelTextSize(8f);
        overallProgess.getLegend().setEnabled(false);
        overallProgess.setTouchEnabled(false);
        overallProgess.setHoleRadius(65f);
        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
        int completed = 0;
        for(Task task : tasks){
            if(task.isCompleted()){
                completed++;
            }
        }

        entries.add(new PieEntry(completed));
        entries.add(new PieEntry(tasks.size()-completed));
        PieDataSet dataSet = new PieDataSet(entries,"");
        dataSet.setValueFormatter(new PieChartValueFormatter());
        dataSet.setColors(new int[]{getActivity().getColor(R.color.colorPrimaryDark),getActivity().getColor(R.color.uncompletedColor)});
        dataSet.setDrawIcons(false);

        PieData data = new PieData(dataSet);
        overallProgess.setData(data);
        overallProgessText.setText(String.valueOf(tasks.size()-completed));

        appBarLayout.invalidate();
        overallProgess.invalidate();


    }

    @Override
    public void onPause() {
        super.onPause();
        ItemClickSupport.removeFrom(mTaskList);
        mPresenter.unsubscribe();
    }

    @Override
    public void setLoadingIndicator(final boolean active) {

        if (getView() == null) {
            return;
        }
        final SwipeRefreshLayout srl =
                (SwipeRefreshLayout) getView().findViewById(R.id.refresh_layout);

        // Make sure setRefreshing() is called after the layout is done with everything else.
        srl.post(new Runnable() {
            @Override
            public void run() {
                srl.setRefreshing(active);
            }
        });
    }

    @Override
    public void showTasks(List<Task> tasks) {
        mOverviewAdapter.replaceData(tasks);
        setOverallProgess(tasks);
        mTasksView.setVisibility(View.VISIBLE);
        mNoTasksView.setVisibility(View.GONE);
    }

    @Override
    public void showAddTask() {
        Intent intent = new Intent(getContext(), TaskDetailActivity.class);
        startActivityForResult(intent, TaskDetailActivity.REQUEST_ADD_TASK);
    }

    @Override
    public void openTaskGroup(String groupName) {
        // in it's own Activity, since it makes more sense that way and it gives us the flexibility
        // to show some Intent stubbing.
        Intent intent = new Intent(getContext(), TaskListActivity.class);
        intent.putExtra(TaskListActivity.PARAM_GROUP_NAME, groupName);
        startActivity(intent);
    }


    @Override
    public void showLoadingTasksError() {
        showMessage(getString(R.string.loading_tasks_error));
    }


    @Override
    public void showNoTasks() {
        showNoTasksViews(
                getResources().getString(R.string.no_tasks_all),
                R.drawable.ic_assignment_turned_in_24dp,
                false
        );
    }


    @Override
    public void setPresenter(OverviewContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }


    private void showMessage(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }

    private void showNoTasksViews(String mainText, int iconRes, boolean showAddView) {
        mTasksView.setVisibility(View.GONE);
        mNoTasksView.setVisibility(View.VISIBLE);

        mNoTaskMainView.setText(mainText);
        mNoTaskIcon.setImageDrawable(getResources().getDrawable(iconRes));
    }
}
