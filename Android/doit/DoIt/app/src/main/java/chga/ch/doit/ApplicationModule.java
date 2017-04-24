package chga.ch.doit;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

/**
 * Created by chsc on 25.03.17.
 *
 * This is a Dagger component. We use this to pass in the context dependency to
 * the {@link chga.ch.doit.data.TasksRepositoryComponent}
 */

@Module
public final class ApplicationModule {

    private Context context;

    public ApplicationModule(Context context){
        this.context = context;
    }

    @Provides
    Context provideContext(){
        return context;
    }
}
