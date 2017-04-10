package ch.schoeb.exercise05_adapterview;

import android.app.Activity;

import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        ListView listView = (ListView)findViewById(R.id.listView);
        Button button = (Button) findViewById(R.id.addPersonBtn);

        final PersonAdapter adapter = new PersonAdapter(this, PersonRepository.getPersons());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Person person = (Person) adapter.getItem(i);
                Toast.makeText(MainActivity.this,String.format("Person with name %1s was clicked",person.getName()),Toast.LENGTH_SHORT).show();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PersonRepository.addRandomPerson();
                adapter.notifyDataSetChanged();
            }
        });

    }

}
