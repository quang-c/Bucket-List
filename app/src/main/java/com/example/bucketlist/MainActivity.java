package com.example.bucketlist;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements RecyclerView.OnItemTouchListener {

    private RecyclerView mRecyclerView;
    private List<BucketItem> bucketList;
    private BucketListAdapter bucketListAdapter;
    private GestureDetector mGestureDetector;
    private BucketItemRoomDatabase db;
    private Executor executor = Executors.newSingleThreadExecutor();

    public static final String NEW_BUCKET_ITEM = "NewItem";
    public static final int REQUESTCODE = 4321;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = BucketItemRoomDatabase.getDatabase(this);
        bucketList = new ArrayList<>();
        bucketListAdapter = new BucketListAdapter(bucketList);

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(bucketListAdapter);
        // seperator line
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddBucketItem.class);
                startActivityForResult(intent, REQUESTCODE);
            }
        });

        mGestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public void onLongPress(MotionEvent e) {
                super.onLongPress(e);
                View child = mRecyclerView.findChildViewUnder(e.getX(), e.getY());
                if (child != null) {
                    int adapterPosition = mRecyclerView.getChildAdapterPosition(child);
                    deleteBucketItem(bucketList.get(adapterPosition));
                }
            }
        });

        mRecyclerView.addOnItemTouchListener(this);
        getAllBucketItems();
    }

    private void deleteBucketItem(final BucketItem bucketItem) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                db.bucketItemDao().delete(bucketItem);
                getAllBucketItems();
            }
        });
    }

    private void deleteAllBucketItems(final List<BucketItem> bucketItems) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                db.bucketItemDao().delete(bucketItems);
                getAllBucketItems();
            }
        });
    }

    private void getAllBucketItems() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                final List<BucketItem> items = db.bucketItemDao().getAllBucketItems();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateUI(items);
                    }
                });
            }
        });
    }

    private void insertBucketItem(final BucketItem bucketItem) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                db.bucketItemDao().insert(bucketItem);
                getAllBucketItems();
            }
        });
    }

    private void updateUI(List<BucketItem> bucketItems) {
        bucketList.clear();
        bucketList.addAll(bucketItems);
        bucketListAdapter.notifyDataSetChanged();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.deleteItems) {
            deleteAllBucketItems(bucketList);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
        mGestureDetector.onTouchEvent(motionEvent);
        return false;
    }

    @Override
    public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean b) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUESTCODE) {
            if (resultCode == RESULT_OK) {
                BucketItem addBucketItem = data.getParcelableExtra(MainActivity.NEW_BUCKET_ITEM);
                insertBucketItem(addBucketItem);
            }
        }
    }
}
