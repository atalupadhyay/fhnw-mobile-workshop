package chga.ch.doit.ui.list;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import chga.ch.doit.R;
import chga.ch.doit.model.Task;
import chga.ch.doit.ui.detail.TaskDetailActivity;
import chga.ch.doit.ui.overview.PieChartValueFormatter;
import chga.ch.doit.ui.overview.ScrollChildSwipeRefreshLayout;
import chga.ch.doit.util.DateUtils;
import chga.ch.doit.util.ItemClickSupport;

/**
 * Created by chsc on 20.04.17.
 */

public class TaskListFragment extends Fragment implements TaskListContract.View {

    TaskListContract.Presenter mPresenter;

    @InjectView(R.id.tasks_list)
    RecyclerView mTaskList;

    TextView currentDate;
    PieChart overallProgess;
    AppBarLayout appBarLayout;
    TextView overallProgessText;
    TaskListAdapter mListAdapter;
    TextView completedTextView;
    TextView dueTodayTextView;
    TextView overdueTextView;
    private Paint p = new Paint();


    @NonNull
    private static final String ARGUMENT_GROUP_NAME = "GROUP_NAME";

    public TaskListFragment(){}

    public static TaskListFragment newInstance(@NonNull String groupName) {
        Bundle arguments = new Bundle();
        arguments.putString(ARGUMENT_GROUP_NAME, groupName);
        TaskListFragment fragment = new TaskListFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListAdapter = new TaskListAdapter(getActivity());
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.inject(this,view);


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
        completedTextView = (TextView) getActivity().findViewById(R.id.completedValue);
        dueTodayTextView = (TextView) getActivity().findViewById(R.id.todayValue);
        overdueTextView = (TextView) getActivity().findViewById(R.id.overdueValue);
        initSwipe();
        return view;
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Setup the recyclerview
        mTaskList.setAdapter(mListAdapter);
        mTaskList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mTaskList.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL_LIST,R.drawable.standart_divider));

    }

    @Override
    public void onResume() {
        super.onResume();
        ItemClickSupport.addTo(mTaskList).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                String taskIdForPosition = mListAdapter.getTaskIdForPosition(position);
                mPresenter.openTask(taskIdForPosition);
            }
        });
        mPresenter.subscribe();
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
    public void openTask(String taskId) {
        Intent intent = new Intent(getContext(), TaskDetailActivity.class);
        intent.putExtra(TaskDetailActivity.PARAM_TASK_ID, taskId);
        startActivity(intent);
    }


    private void setOverallProgess(List<Task> tasks){
        Date now = new Date();
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
        int overdue = 0;
        int today = 0;
        for(Task task : tasks){
            if(task.isCompleted()){
                completed++;
            }else if(task.getDate()!= null){
                if(DateUtils.isSameDay(now,task.getDate())){
                    today++;
                }else if(task.getDate().before(now)){
                    overdue++;
                }
            }

        }



        entries.add(new PieEntry(completed));
        entries.add(new PieEntry(tasks.size()-completed));
        PieDataSet dataSet = new PieDataSet(entries,"");
        dataSet.setValueFormatter(new PieChartValueFormatter());
        dataSet.setColors(new int[]{getActivity().getColor(R.color.colorPrimaryDark),getActivity().getColor(R.color.uncompletedColor)});
        dataSet.setDrawIcons(false);
        PieData data = new PieData(dataSet);
        completedTextView.setText(String.valueOf(completed));
        overdueTextView.setText(String.valueOf(overdue));
        dueTodayTextView.setText(String.valueOf(today));
        overallProgess.setData(data);
        overallProgessText.setText(String.valueOf(tasks.size()-completed));

        appBarLayout.invalidate();
        overallProgess.invalidate();
    }

    @Override
    public void showTasks(List<Task> tasks) {
        setOverallProgess(tasks);
        mListAdapter.replaceData(tasks);
    }



    private void initSwipe(){
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                if (direction == ItemTouchHelper.LEFT){
                   mPresenter.deleteTask(mListAdapter.getTaskIdForPosition(position));
                } else {
                   mPresenter.completeTask(mListAdapter.getTaskIdForPosition(position));
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                Bitmap icon;
                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){

                    View itemView = viewHolder.itemView;
                    boolean isCompleted = (boolean) itemView.getTag(R.id.TAG_TASK_LIST_IS_COMPLETED);
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if(dX > 0){
                        if(isCompleted){
                            return;
                        }
                        p.setColor((getContext().getColor(R.color.task_list_completed)));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX,(float) itemView.getBottom());
                        c.drawRect(background,p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_checkbox_marked_circle_white_48dp);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width ,(float) itemView.getTop() + width,(float) itemView.getLeft()+ 2*width,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p);
                    } else {
                        p.setColor(getContext().getColor(R.color.task_list_uncompleted));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background,p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_delete_white_48dp);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2*width ,(float) itemView.getTop() + width,(float) itemView.getRight() - width,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mTaskList);
    }



    @Override
    public void setPresenter(TaskListContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }


}
