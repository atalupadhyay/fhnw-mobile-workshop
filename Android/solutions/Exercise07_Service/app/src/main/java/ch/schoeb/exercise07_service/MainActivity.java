package ch.schoeb.exercise07_service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private CounterService counterService;
    private TextView currentCounterValueTextView;

    private CounterService.CounterServiceListener listener = new CounterService.CounterServiceListener() {
        @Override
        public void longRunningOperationCallback() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity.this,"Long running operation finished",Toast.LENGTH_SHORT).show();
                }
            });
        }
    };

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            CounterService.CounterServiceBinder customBinder = (CounterService.CounterServiceBinder)service;
            counterService = customBinder.getService();
            counterService.registerListener(listener);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentCounterValueTextView = (TextView)findViewById(R.id.textViewCurrentCounter);

        Button increaseButton = (Button) findViewById(R.id.buttonIncrease);
        increaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increaseCounterOnService();
            }
        });

        Button readButton = (Button)findViewById(R.id.buttonReadCounter);
        readButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readCounterValue();
            }
        });

        Button longRunningOpButton = (Button) findViewById(R.id.longRunningOperation);
        longRunningOpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                longRunningOperation();
            }
        });

        startService(new Intent(this, CounterService.class));

    }

    @Override
    protected void onResume() {
        bindService(new Intent(this,CounterService.class),serviceConnection, Service.BIND_AUTO_CREATE);
        super.onResume();
    }

    @Override
    protected void onPause() {
        counterService.deRegisterListener(listener);

        unbindService(serviceConnection);
        super.onPause();
    }

    private void longRunningOperation(){
        counterService.longRunningOperation();
    }

    private void increaseCounterOnService() {
        counterService.increaseCounter();
    }

    private void readCounterValue() {
       currentCounterValueTextView.setText(String.valueOf(counterService.getCounter()));
    }
}
