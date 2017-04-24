package chga.ch.doit.ui.overview;

import android.content.ClipData;
import android.support.v7.util.DiffUtil;

import java.util.List;

/**
 * Created by chsc on 23.04.17.
 *
 *
 * This is helper class helps to schedule fine grained change events to {@link OverviewAdapter},
 * so we do not need to call notifyDataSetChanged() inside the adapter, which greatly increases
 * the memory usage.
 */

public class GroupItemHolderDIffCallback extends DiffUtil.Callback {

    private List<OverviewAdapter.ItemHolder> oldItems;
    private List<OverviewAdapter.ItemHolder> newItems;


    public GroupItemHolderDIffCallback(List<OverviewAdapter.ItemHolder> oldItems,List<OverviewAdapter.ItemHolder> newItems){
        this.oldItems = oldItems;
        this.newItems = newItems;
    }

    @Override
    public int getOldListSize() {
        return oldItems.size();
    }

    @Override
    public int getNewListSize() {
        return newItems.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        OverviewAdapter.ItemHolder oldItem = oldItems.get(oldItemPosition);
        OverviewAdapter.ItemHolder newItem = newItems.get(newItemPosition);
        if(oldItem != null && newItem != null){
            return oldItem.getGroupTitle().equals(newItem.getGroupTitle());
        }
        return false;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        OverviewAdapter.ItemHolder oldItem = oldItems.get(oldItemPosition);
        OverviewAdapter.ItemHolder newItem = newItems.get(newItemPosition);
        if(oldItem != null && newItem != null){
            return oldItem.getTasks().containsAll(newItem.getTasks()) &&
                    newItem.getTasks().containsAll(oldItem.getTasks());

        }
        return false;
    }
}
