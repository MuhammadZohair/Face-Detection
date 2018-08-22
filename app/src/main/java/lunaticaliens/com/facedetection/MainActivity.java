package lunaticaliens.com.facedetection;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Project Title: Custom Alert Dialog
 * Created on: 31/07/2018
 * Created By: Muhammad Zohair
 * Description: This project implements the Face Detection module. Different dataset is preloaded in the
 * application on which user can run facial detection. Please keep in mind that it is
 * face detection not face recognition
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button preloadedImagesButton, customImagesButton; // two buttons to open the respective activities

    /**
     * This method is called as soon as the activity is created in the memory
     *
     * @param savedInstanceState is used to transfer content between activities
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preloadedImagesButton = findViewById(R.id.preloadedImagesButton);
        customImagesButton = findViewById(R.id.customImagesButton);

        preloadedImagesButton.setOnClickListener(this);
        customImagesButton.setOnClickListener(this);
    }

    /**
     * This method is called whenever any widget is clicked
     * @param view to get the shown view
     */
    @Override
    public void onClick(View view) {
        if (view == preloadedImagesButton) {
            finish();
            startActivity(new Intent(MainActivity.this, PreloadedImagesDetectionActivity.class));

        }
        if (view == customImagesButton) {
            finish();
            startActivity(new Intent(MainActivity.this, CustomImageActivity.class));
        }
    }
}
