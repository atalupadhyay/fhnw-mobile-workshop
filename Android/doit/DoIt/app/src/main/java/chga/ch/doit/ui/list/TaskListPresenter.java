package chga.ch.doit.ui.list;

import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;

import chga.ch.doit.BasePresenter;
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
 * Listens to user actions from the UI ({@link TaskListFragment}), retrieves the data and updates the
 * UI as required.
 * <p />
 * By marking the constructor with {@code @Inject}, Dagger injects the dependencies required to
 * create an instance of the TasksPresenter (if it fails, it emits a compiler error).  It uses
 * {@link TaskListPresenterModule} to do so.
 * <p/>
 * Dagger generated code doesn't require public access to the constructor or class, and
 * therefore, to ensure the developer doesn't instantiate the class manually and bypasses Dagger,
 * it's good practice minimise the visibility of the class/constructor as much as possible.
 **/
public class TaskListPresenter implements TaskListContract.Presenter {

    private final TasksRepository mTasksRepository;

    private final TaskListContract.View mTasksView;
    private final BaseSchedulerProvider mSchedulerProvider;
    private final CompositeSubscription mSubscriptions;
    private final String groupName;


    private boolean mFirstLoad = true;

    @Inject
    public TaskListPresenter(@NonNull TasksRepository mTasksRepository,
                             @NonNull TaskListContract.View mTasksView,
                             @NonNull @Scheduler BaseSchedulerProvider mSchedulerProvider,
                             @NonNull String groupName) {
        this.mTasksRepository = mTasksRepository;
        this.mTasksView = mTasksView;
        this.mSchedulerProvider = mSchedulerProvider;
        this.mSubscriptions = new CompositeSubscription();
        this.groupName  = groupName;
    }

    /**
     * Method injection is used here to safely reference {@code this} after the object is created.
     * For more information, see Java Concurrency in Practice.
     */
    @Inject
    void setupListeners() {
        mTasksView.setPresenter(this);
    }

    @Override
    public void loadTasks(boolean forceUpdate) {
        // Simplification for sample: a network reload will be forced on first load.
        loadTasks(forceUpdate || mFirstLoad, true);
        mFirstLoad = false;
    }


    /**
     * @param forceUpdate   Pass in true to refresh the data in the {@link chga.ch.doit.data.TasksDataSource}
     * @param showLoadingUI Pass in true to display a loading icon in the UI
     */
    private void loadTasks(final boolean forceUpdate, final boolean showLoadingUI) {

        if (showLoadingUI) {
            mTasksView.setLoadingIndicator(true);
        }

        if (forceUpdate) {
            mTasksRepository.refreshTasks();
        }


        mSubscriptions.clear();
        Subscription subscription = mTasksRepository
                .getTasks()
                .flatMap(new Func1<List<Task>, Observable<Task>>() {
                    @Override
                    public Observable<Task> call(List<Task> tasks) {
                        return Observable.from(tasks);
                    }
                })
                .filter(new Func1<Task, Boolean>() {
                    @Override
                    public Boolean call(Task task) {
                        if(task.getGroup() == null || task.getGroup().isEmpty()){
                            return false;
                        }else if(task.getGroup().equals(groupName)){
                            return true;
                        }else{
                            return false;
                        }

                    }
                })
                .subscribeOn(mSchedulerProvider.computation())
                .observeOn(mSchedulerProvider.ui())
                .toList()
                .subscribe(new Action1<List<Task>>() {
                    @Override
                    public void call(List<Task> tasks) {
                        mTasksView.showTasks(tasks);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                       // TODO show error
                    }
                }, new Action0() {
                    @Override
                    public void call() {
                        mTasksView.setLoadingIndicator(false);
                    }
                });


        mSubscriptions.add(subscription);
    }

    @Override
    public void openTask(String taskId) {
        checkNotNull(taskId);
        mTasksView.openTask(taskId);
    }

    @Override
    public void completeTask(String taskId) {
        mTasksRepository.completeTask(taskId);
        loadTasks(true);
    }

    @Override
    public void deleteTask(String taskId) {
        mTasksRepository.deleteTask(taskId);
       loadTasks(true);
    }

    @Override
    public void subscribe() {
        loadTasks(true);
    }

    @Override
    public void unsubscribe() {
        mSubscriptions.clear();
    }
}
