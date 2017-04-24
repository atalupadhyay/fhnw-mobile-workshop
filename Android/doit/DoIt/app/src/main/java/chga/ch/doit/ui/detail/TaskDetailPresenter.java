package chga.ch.doit.ui.detail;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.common.base.Strings;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import chga.ch.doit.data.TasksRepository;
import chga.ch.doit.model.Task;
import chga.ch.doit.util.scheduler.BaseSchedulerProvider;
import chga.ch.doit.util.scheduler.Scheduler;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.subscriptions.CompositeSubscription;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Listens to user actions from the UI ({@link TaskDetailFragment}), retrieves the data and updates the
 * UI as required.
 * <p />
 * By marking the constructor with {@code @Inject}, Dagger injects the dependencies required to
 * create an instance of the TasksPresenter (if it fails, it emits a compiler error).  It uses
 * {@link TaskDetailPresenterModule} to do so.
 * <p/>
 * Dagger generated code doesn't require public access to the constructor or class, and
 * therefore, to ensure the developer doesn't instantiate the class manually and bypasses Dagger,
 * it's good practice minimise the visibility of the class/constructor as much as possible.
 **/
public class TaskDetailPresenter implements TaskDetailContract.Presenter {



    @NonNull
    private final TasksRepository mTasksRepository;

    @NonNull
    private final TaskDetailContract.View mDetailView;

    @NonNull
    private final BaseSchedulerProvider mSchedulerProvider;

    @Nullable
    private String mTaskId;

    @NonNull
    private CompositeSubscription mSubscriptions;

    @Inject
    public TaskDetailPresenter(@Nullable String taskId,
                               @NonNull TasksRepository tasksRepository,
                               @NonNull TaskDetailContract.View detailView,
                               @NonNull @Scheduler BaseSchedulerProvider schedulerProvider) {
        this.mTaskId = taskId;
        mTasksRepository = checkNotNull(tasksRepository, "tasksRepository cannot be null!");
        mDetailView = checkNotNull(detailView, "taskDetailView cannot be null!");
        mSchedulerProvider = checkNotNull(schedulerProvider, "schedulerProvider cannot be null");

        mSubscriptions = new CompositeSubscription();
    }

    /**
     * Method injection is used here to safely reference {@code this} after the object is created.
     * For more information, see Java Concurrency in Practice.
     */
    @Inject
    void setupListeners() {
        mDetailView.setPresenter(this);
    }


    @Override
    public void saveTask(String title, String description, String group,Date date){
        if (isNewTask()) {
            createTask(title, description,group,date);
        } else {
            updateTask(title, description,group,date);
        }
    }

    @Override
    public void loadGroups() {
        Subscription subscription = mTasksRepository
                .getTasks()
                .flatMap(new Func1<List<Task>, Observable<Task>>() {
                    @Override
                    public Observable<Task> call(List<Task> tasks) {
                        return Observable.from(tasks);
                    }
                })
                .subscribeOn(mSchedulerProvider.computation())
                .observeOn(mSchedulerProvider.ui())
                .toList()
                .subscribe(new Action1<List<Task>>() {
                    @Override
                    public void call(List<Task> tasks) {
                        List<String> groups = new ArrayList<String>();
                        for (Task task : tasks) {
                            groups.add(task.getGroup());
                        }
                        mDetailView.showGroups(groups);
                    }
                });


        mSubscriptions.add(subscription);
    }

    @Override
    public void subscribe() {
        openTask();
        loadGroups();
    }

    @Override
    public void unsubscribe() {
        mSubscriptions.clear();
    }


    private boolean isNewTask() {
        return mTaskId == null;
    }

    private void openTask() {
        if (Strings.isNullOrEmpty(mTaskId)) {
            return;
        }

        mSubscriptions.add(mTasksRepository
                .getTask(mTaskId)
                .subscribeOn(mSchedulerProvider.computation())
                .observeOn(mSchedulerProvider.ui())
                .subscribe(new Action1<Task>() {
                               @Override
                               public void call(Task task) {
                                        mDetailView.showTask(task.getTitle(), task.getDescription(), task.getGroup(),task.getDate());
                               }
                           }
               ));
    }

    private void createTask(String title, String description,String group,Date date) {
        Task newTask = new Task(UUID.randomUUID().toString(),title, description,group,date,false);
        if (newTask.isEmpty()) {
           return;
        } else {
            mTasksRepository.saveTask(newTask);
            mDetailView.showTasksList();
        }
    }

    private void updateTask(String title, String description,String group,Date date) {
        if (isNewTask()) {
            throw new RuntimeException("updateTask() was called but task is new.");
        }
        mTasksRepository.updateTask(new Task(mTaskId,title, description,group,date,false));
        mDetailView.showTasksList(); // After an edit, go back to the list.
    }


}
