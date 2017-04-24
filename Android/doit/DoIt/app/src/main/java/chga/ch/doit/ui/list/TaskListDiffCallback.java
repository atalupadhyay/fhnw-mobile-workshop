package chga.ch.doit.ui.list;

import android.support.v7.util.DiffUtil;

import java.util.List;

import chga.ch.doit.model.Task;

/**
 * Created by chsc on 22.04.17.
 */

public class TaskListDiffCallback extends DiffUtil.Callback {

    private final List<Task> oldTasks;
    private final List<Task> newTasks;

    public TaskListDiffCallback(List<Task> oldTaks,List<Task> newTasks) {
        this.oldTasks = oldTaks;
        this.newTasks = newTasks;
    }


    @Override
    public int getOldListSize() {
        return oldTasks.size();
    }

    @Override
    public int getNewListSize() {
        return newTasks.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        Task oldTask = oldTasks.get(oldItemPosition);
        Task newTask = newTasks.get(newItemPosition);
        if(oldTask!= null && newTask != null){
            return oldTask.getId().equals(newTask.getId());
        }
        return false;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        Task oldTask = oldTasks.get(oldItemPosition);
        Task newTask = newTasks.get(newItemPosition);
        if(oldTask != null && newTask != null){
            return oldTask.equals(newTask);
        }
        return false;
    }
}
