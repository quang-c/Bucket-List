package com.example.bucketlist;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AddBucketItem extends AppCompatActivity {

    private TextView title;
    private TextView description;
    private Button createButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bucket);

        title = findViewById(R.id.editTitle);
        description = findViewById(R.id.editDescription);
        createButton = findViewById(R.id.button);

        createButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                String titleText = title.getText().toString();
                String descriptionText = description.getText().toString();


                if (!TextUtils.isEmpty(titleText) && !TextUtils.isEmpty(descriptionText)){
                    BucketItem bucketItem = new BucketItem(titleText, descriptionText);
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra(MainActivity.NEW_BUCKET_ITEM, bucketItem);
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                }
                else
                    Snackbar.make(view, "Please fill in the fields", Snackbar.LENGTH_LONG).setAction("Action", null).show();

            }
        });
    }
}

