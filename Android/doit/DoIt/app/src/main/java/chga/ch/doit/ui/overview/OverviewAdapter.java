package chga.ch.doit.ui.overview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import chga.ch.doit.R;
import chga.ch.doit.model.Task;
import chga.ch.doit.ui.list.TaskListActivity;
import chga.ch.doit.ui.list.TaskListAdapter;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by chsc on 15.04.17.
 */

public class OverviewAdapter extends RecyclerView.Adapter<OverviewAdapter.OverviewHolder> {

    private List<ItemHolder> items = new ArrayList<>();
    private Context context;

    public OverviewAdapter(Context context){
        this.context = context;
    }

    public void replaceData(List<Task> tasks) {
        List<ItemHolder> newItems = parseTasks(checkNotNull(tasks));
        new ItemHolderDiffTask(newItems).execute();
    }

    private List<ItemHolder> parseTasks(List<Task> tasks) {
        Map<String, ItemHolder> itemHolderMap = new HashMap<>();

        for (Task aTask : tasks) {
                if(!itemHolderMap.containsKey(aTask.getGroup())){
                    ItemHolder holder = new ItemHolder();
                    holder.groupTitle = aTask.getGroup();
                    itemHolderMap.put(aTask.getGroup(),holder);
                }
                itemHolderMap.get(aTask.getGroup()).tasks.add(aTask);

        }
        return new ArrayList<>(itemHolderMap.values());
    }


    @Override
    public OverviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        OverviewHolder viewHolder;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_overview, parent, false);
        viewHolder = new OverviewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final OverviewHolder holder, int position) {
        if(!items.isEmpty()){
            final ItemHolder itemHolder = items.get(position);

            if (itemHolder != null) {
                holder.groupTitle.setText(itemHolder.groupTitle.trim());
                holder.pieChart.setUsePercentValues(true);
                holder.pieChart.setDrawHoleEnabled(true);
                holder.pieChart.setHoleColor(Color.WHITE);
                holder.pieChart.setTransparentCircleColor(Color.WHITE);
                holder.pieChart.setTransparentCircleAlpha(0);
                holder.pieChart.getDescription().setEnabled(false);
                holder.pieChart.setDrawEntryLabels(true);
                holder.pieChart.setCenterTextColor(Color.WHITE);
                holder.pieChart.setEntryLabelTextSize(8f);
                holder.pieChart.getLegend().setEnabled(false);
                holder.pieChart.setTouchEnabled(false);
                holder.pieChart.setHoleRadius(65f);
                ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
                int completed = 0;
                for(Task task : itemHolder.tasks){
                    if(task.isCompleted()){
                        completed++;
                    }
                }
                entries.add(new PieEntry(completed));
                entries.add(new PieEntry(itemHolder.tasks.size()-completed));
                PieDataSet dataSet = new PieDataSet(entries,"");
                dataSet.setValueFormatter(new PieChartValueFormatter());
                dataSet.setColors(new int[]{context.getColor(R.color.completedColor),context.getColor(R.color.uncompletedColor)});
                dataSet.setDrawIcons(false);
                PieData data = new PieData(dataSet);
                holder.pieChart.setData(data);

                holder.itemCount.setText(String.valueOf(itemHolder.tasks.size()-completed));
            }
        }

    }

    public String getGroupTitleForPosition(int position){
        if( !items.isEmpty() && position < items.size()){
            return items.get(position).getGroupTitle();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class OverviewHolder extends RecyclerView.ViewHolder {

        TextView groupTitle;
        TextView itemCount;
        TextView itemCountLabel;
        PieChart pieChart;

        public OverviewHolder(View itemView) {
            super(itemView);
            groupTitle = (TextView) itemView.findViewById(R.id.groupId);
            itemCount = (TextView) itemView.findViewById(R.id.itemcount_textview);
            pieChart = (PieChart) itemView.findViewById(R.id.piechart);
            itemCountLabel = (TextView) itemView.findViewById(R.id.itemcount_textview_label);

        }
    }

    private class ItemHolderDiffTask extends AsyncTask<Void,Void,DiffUtil.DiffResult>{

        private List<ItemHolder> newItems;

        public ItemHolderDiffTask(List<ItemHolder> newItems){
            this.newItems = newItems;
        }

        @Override
        protected DiffUtil.DiffResult doInBackground(Void... params) {
            Collections.sort(newItems, new Comparator<ItemHolder>() {
                @Override
                public int compare(ItemHolder o1, ItemHolder o2) {
                    return o1.getGroupTitle().compareTo(o2.getGroupTitle());
                }
            });

            final GroupItemHolderDIffCallback callback = new GroupItemHolderDIffCallback(items, newItems);
            final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(callback);

            return diffResult;
        }

        @Override
        protected void onPostExecute(DiffUtil.DiffResult diffResult) {
            super.onPostExecute(diffResult);
            // Apply the new model
            items.clear();
            items.addAll(newItems);
            diffResult.dispatchUpdatesTo(OverviewAdapter.this);
        }
    }


    public class ItemHolder{

        private String groupTitle;
        private List<Task> tasks = new ArrayList<>();

        public String getGroupTitle() {
            return groupTitle;
        }

        public List<Task> getTasks() {
            return tasks;
        }
    }


}
