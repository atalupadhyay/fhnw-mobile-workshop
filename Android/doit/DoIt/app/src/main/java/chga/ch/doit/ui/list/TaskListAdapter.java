package chga.ch.doit.ui.list;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import chga.ch.doit.R;
import chga.ch.doit.model.Task;
import chga.ch.doit.util.DateUtils;

/**
 * Created by chsc on 21.04.17.
 */

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.TaskListViewHolder> {


    private final List<Task> tasks = new ArrayList<>();
    private final Context context;

    public TaskListAdapter(Context context) {
        this.context = context;
    }


    public void replaceData(List<Task> newTasks) {
        new TaskDiffTask(newTasks).execute();
    }

    public String getTaskIdForPosition(int position) {
        if (!tasks.isEmpty() && position <= tasks.size()) {
            return tasks.get(position).getId();
        }
        return null;
    }


    @Override
    public void onBindViewHolder(TaskListViewHolder holder, int position) {
        if (tasks.size() > 0) {
            Task task = tasks.get(position);
            holder.taskTitle.setText(task.getTitle().trim());
            holder.taskDescription.setText(task.getDescription().trim());
            holder.isCompleted.setColorFilter(ContextCompat.getColor(context, (task.isCompleted() ? R.color.task_list_completed : R.color.uncompletedColor)));
            holder.itemView.setTag(R.id.TAG_TASK_LIST_IS_COMPLETED, (boolean) task.isCompleted());
            setDateLabelAndValue(task, holder);

        }

    }

    @Override
    public TaskListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TaskListViewHolder viewHolder;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        viewHolder = new TaskListViewHolder(view);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }


    private void setDateLabelAndValue(Task task, TaskListViewHolder holder) {
        Date now = new Date();
        if (task.getDate() == null || task.isCompleted()) {
            holder.dateLabel.setText("");
            holder.dateValue.setText("");
        }else if (DateUtils.isToday(task.getDate())) {
            holder.dateLabel.setText(context.getString(R.string.task_list_date_label_today));
            holder.dateLabel.setTextColor(context.getColor(R.color.task_list_completed));
            holder.dateValue.setText("");

        }else if (task.getDate().before(now)) {
            holder.dateLabel.setText(context.getString(R.string.task_list_date_label_overdue));
            holder.dateLabel.setTextColor(context.getColor(R.color.task_list_uncompleted));
            holder.dateValue.setText(Math.abs(DateUtils.getHoursDifference(task.getDate(), now)) + " " + context.getString(R.string.task_list_date_hours));

        } else  {
            holder.dateLabel.setText(context.getString(R.string.task_list_date_label_future));
            holder.dateLabel.setTextColor(context.getColor(R.color.task_list_completed));
            holder.dateValue.setText(DateUtils.getHoursDifference(task.getDate(), now) +" "+context.getString(R.string.task_list_date_hours));
        }
    }


    static class TaskListViewHolder extends RecyclerView.ViewHolder {

        private TextView taskTitle;
        private TextView taskDescription;
        private ImageView isCompleted;
        private TextView dateLabel;
        private TextView dateValue;

        public TaskListViewHolder(View itemView) {
            super(itemView);
            taskTitle = (TextView) itemView.findViewById(R.id.taskTitle);
            taskDescription = (TextView) itemView.findViewById(R.id.taskDescription);
            isCompleted = (ImageView) itemView.findViewById(R.id.isCompleted);
            dateLabel = (TextView) itemView.findViewById(R.id.dateLabel);
            dateValue = (TextView) itemView.findViewById(R.id.dateValue);
        }
    }

    private class TaskDiffTask extends AsyncTask<Void, Void, DiffUtil.DiffResult> {

        private List<Task> newTasks;

        public TaskDiffTask(List<Task> newTasks) {
            this.newTasks = newTasks;
        }

        @Override
        protected DiffUtil.DiffResult doInBackground(Void... params) {
            Collections.sort(newTasks, new Comparator<Task>() {
                @Override
                public int compare(Task o1, Task o2) {
                    if (o1.getDate() != null && o2.getDate() != null) {
                        return o1.getDate().compareTo(o2.getDate());
                    }
                    return 0;
                }
            });
            final TaskListDiffCallback callback = new TaskListDiffCallback(tasks, newTasks);
            final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(callback);

            return diffResult;
        }


        @Override
        protected void onPostExecute(DiffUtil.DiffResult diffResult) {
            super.onPostExecute(diffResult);
            // Apply the new model
            tasks.clear();
            tasks.addAll(newTasks);
            diffResult.dispatchUpdatesTo(TaskListAdapter.this);
        }
    }

}
