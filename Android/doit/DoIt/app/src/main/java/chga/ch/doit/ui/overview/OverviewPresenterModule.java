package chga.ch.doit.ui.overview;

/**
 * Created by chsc on 15.04.17.
 */

import dagger.Module;
import dagger.Provides;

/**
 * This is a Dagger module. We use this to pass in the View dependency to the
 * {@link OverviewPresenter}.
 */
@Module
public class OverviewPresenterModule {

    private final OverviewContract.View mView;

    public OverviewPresenterModule(OverviewContract.View view) {
        mView = view;
    }

    @Provides
    OverviewContract.View provideTasksContractView() {
        return mView;
    }

}
