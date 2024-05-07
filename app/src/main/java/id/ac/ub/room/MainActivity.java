package id.ac.ub.room;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button bt1;
    EditText et1;
    RecyclerView recyclerView;
    private AppDatabase appDb;
    private ItemAdapter adapter;
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        appDb = AppDatabase.getInstance(getApplicationContext());
        bt1 = findViewById(R.id.bt1);
        et1 = findViewById(R.id.et1);
        recyclerView = findViewById(R.id.rv_recyclerView);

        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Item item = new Item();
                item.setJudul(et1.getText().toString());
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        appDb.itemDao().insertAll(item);

                        List<Item> updatedList = appDb.itemDao().getAll();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.updateData(updatedList);
                                et1.setText("");
                            }
                        });
                    }
                });
            }
        });

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                final List<Item> list = appDb.itemDao().getAll();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter = new ItemAdapter(list);
                        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                        recyclerView.setAdapter(adapter);
                    }
                });
            }
        });
    }
}