package chga.ch.doit;

import android.app.Application;
import android.content.Context;

import dagger.Component;

/**
 * Created by chsc on 29.03.17.
 *
 * This is a dagger component.
 *
 * Exposes {@link Application} to any {@link Component} which depends on it.
 */

@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    Context getContext();
}
