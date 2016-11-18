package com.wkswind.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.liulishuo.filedownloader.FileDownloader;

public class MainActivity extends AppCompatActivity {
    private OnlineItem item;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_launch);
        Bundle extras = getIntent().getExtras();
        if(extras != null && extras.containsKey(OnlineItem.class.getName())) {
            String path = extras.getString(OnlineItem.class.getName());
            Utils.launch(this, path);
            finish();
        }else {
            item = OnlineItem.fakeItem();
            findViewById(R.id.launch_game).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onLaunchGameClick(item);
                }
            });
            FileDownloader.getImpl().bindService();
        }

    }

    public void onLaunchGameClick(OnlineItem item) {
        Intent intent = new Intent(this, InstallActivity.class);
        intent.putExtra(OnlineItem.class.getName(), item);
        startActivity(intent);
//        InstallFragment fragment = InstallFragment.newInstance(item);
//        fm.beginTransaction().add(android.R.id.content, fragment, fragment.getClass().getSimpleName()).commit();
    }
}
