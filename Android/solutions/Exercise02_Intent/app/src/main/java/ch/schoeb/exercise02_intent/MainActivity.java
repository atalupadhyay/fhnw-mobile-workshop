package ch.schoeb.exercise02_intent;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn = (Button) findViewById(R.id.buttonNavigate);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,TargetActivity.class);
                intent.putExtra(TargetActivity.EXTRA_KEY,"My data");
                MainActivity.this.startActivity(intent);
            }
        });
        // TODO: Find the button and add a click listener
        // TODO: In the OnClickListener create a new Intent which targets your DetailActivity
        // TODO: Add custom data (Extras) to your intent
        // TODO: Start DetailActivity and show passed data in a textView on the DetailActivity
    }
}
