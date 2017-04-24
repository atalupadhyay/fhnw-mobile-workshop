package chga.ch.doit.ui.detail;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import chga.ch.doit.R;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by chsc on 15.04.17.
 */

public class TaskDetailFragment extends Fragment implements TaskDetailContract.View{

    private static final String TAG = "TaskDetailFragment";

    TaskDetailContract.Presenter mPresenter;

    @InjectView(R.id.titleEdit)
    EditText mTitleField;

    @InjectView(R.id.descriptionEdit)
    EditText mDescriptionField;

    @InjectView(R.id.groupEdit)
    AutoCompleteTextView mGroupField;

    @InjectView(R.id.dateEdit)
    EditText mDateField;

    @NonNull
    private static final String ARGUMENT_TASK_ID = "TASK_ID";

    private static DateFormat dateFormat ;
    private ArrayAdapter<String> mAdapter;

    public TaskDetailFragment(){}



    public static TaskDetailFragment newInstance(@Nullable String taskId) {
        Bundle arguments = new Bundle();
        arguments.putString(ARGUMENT_TASK_ID, taskId);
        TaskDetailFragment fragment = new TaskDetailFragment();
        fragment.setArguments(arguments);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // This is the current set local of the device
        dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT,getResources().getConfiguration().getLocales().get(0));
        mAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.select_dialog_singlechoice, new ArrayList<String>());

    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.subscribe();
    }

    @Override
    public void onPause() {
        super.onPause();
        saveOrDiscardTask();
        mPresenter.unsubscribe();
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.inject(this,view);
        mGroupField.setThreshold(1);
        mGroupField.setAdapter(mAdapter);
        mAdapter.setNotifyOnChange(true);

        return view;
    }


    @Override
    public void showTask(String title, String description, String group,Date date) {
        mTitleField.setText(title);
        mDescriptionField.setText(description);
        mGroupField.setText(group);
        if(date != null){
            mDateField.setText(dateFormat.format(date));
        }

        if(group != null){
            mGroupField.setEnabled(false);
        }


    }

    @OnClick(R.id.dateEdit)
    public void showDatePickerDialog(EditText editText){
        Calendar mcurrentDate=Calendar.getInstance();
        int mYear=mcurrentDate.get(Calendar.YEAR);
        int mMonth=mcurrentDate.get(Calendar.MONTH);
        int mDay=mcurrentDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog mDatePicker=new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                Calendar choosenDate=Calendar.getInstance();
                choosenDate.set(Calendar.YEAR,selectedyear);
                choosenDate.set(Calendar.MONTH,selectedmonth);
                choosenDate.set(Calendar.DAY_OF_MONTH,selectedday);
                Date taskDate = choosenDate.getTime();
                if(taskDate != null){
                    mDateField.setText(dateFormat.format(taskDate));
                }
            }
        },mYear, mMonth, mDay);
        mDatePicker.setTitle(getString(R.string.task_detail_date_picker_title));
        mDatePicker.show();
    }


    @Override
    public void showTasksList() {
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }


    @Override
    public void showGroups(List<String> groups) {
        List<String> existingGroups = new ArrayList<>();
        for(int i = 0;i<mAdapter.getCount();i++){
            existingGroups.add(mAdapter.getItem(i));
        }
        groups.removeAll(existingGroups);

        mAdapter.addAll(new HashSet<String>(groups));
        mAdapter.notifyDataSetChanged();
    }


    @Override
    public void setPresenter(TaskDetailContract.Presenter presenter) {
            mPresenter = checkNotNull(presenter);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    private void saveOrDiscardTask(){
        String titleValue = mTitleField.getText().toString();
        String descriptionValue =  mDescriptionField.getText().toString();
        String group = mGroupField.getText().toString();
        if(group == null){
            group = getString(R.string.overview_item_no_group);
        }
        Date taskDate = null;
        try {
            taskDate = dateFormat.parse(mDateField.getText().toString());
        } catch (ParseException e) {
            Log.e(TAG, "Error while parsing date with: "+e);
        }
        if(titleValue.isEmpty() && descriptionValue.isEmpty()){
            return;
        }
        mPresenter.saveTask(titleValue,descriptionValue,group,taskDate);
    }
}
