package ch.schoeb.exercise02_intent;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;


public class TargetActivity extends AppCompatActivity {

    public static final String EXTRA_KEY = "MyKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_target);
        TextView view = (TextView) findViewById(R.id.textview);
        Intent intent = getIntent();
        if(intent != null){
            String stringArrayExtra = intent.getStringExtra(EXTRA_KEY);
            view.setText(stringArrayExtra);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_target, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id){
            case R.id.action_settings:
            default:
                Toast.makeText(this,"Settings button clicked",Toast.LENGTH_SHORT).show();
                return true;
        }



    }
}
