package ch.schoeb.exercise07_service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.util.LinkedList;
import java.util.List;

public class CounterService extends Service {

    public interface CounterServiceListener{

        void longRunningOperationCallback();
    }

    public class CounterServiceBinder extends Binder {

        public CounterService getService(){
            return CounterService.this;
        }
    }

    private int counter;

    private IBinder binder = new CounterServiceBinder();

    private List<CounterServiceListener> listeners = new LinkedList<>();


    @Override
    public void onCreate() {
        super.onCreate();
        counter = 0;
    }

    @Override
    public IBinder onBind(Intent intent) {
       return binder;
    }

    public void increaseCounter() {
        counter++;
    }

    public int getCounter(){
        return counter;
    }

    public void longRunningOperation(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                for (CounterServiceListener listener : listeners) {
                    listener.longRunningOperationCallback();
                }
            }
        }).start();
    }

    public void registerListener(CounterServiceListener listener){
        listeners.add(listener);
    }

    public void deRegisterListener(CounterServiceListener listener){
        listeners.remove(listener);
    }
}
