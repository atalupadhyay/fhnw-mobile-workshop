package chga.ch.doit;

/**
 * Created by chsc on 25.03.17.
 */

public interface BaseView<T extends BasePresenter> {

    void setPresenter(T presenter);

    boolean isActive();

}
