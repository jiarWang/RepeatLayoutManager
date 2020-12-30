package cn.student0.sample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import cn.student0.manager.RepeatLayoutManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = findViewById(R.id.rv);
        recyclerView.setAdapter(new BeautyAdapter());
        recyclerView.setLayoutManager(new RepeatLayoutManager(RecyclerView.HORIZONTAL));
    }
}