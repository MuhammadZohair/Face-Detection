package lunaticaliens.com.facedetection;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

public class PreloadedImagesDetectionActivity extends AppCompatActivity implements View.OnClickListener {


    private ImageView pictureImageView; // imageview that stores and show the image
    private Button detectFacesButton; // button that perform facial deteciton
    private TextView numberOfFacesTextView; // textview to show total number of faces

    /**
     * This method is called as soon as the activity is created in the memory
     * @param savedInstanceState is used to send content between activities
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preloaded_images_detection); // binds the front end with backend

        pictureImageView = findViewById(R.id.pictureImageView);

        /*
         * This block of code sets the spinner with total images and add the item click listener
         */
        Spinner pictureSelectSpinner = findViewById(R.id.pictureSelectSpinner);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.pictures_name));
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pictureSelectSpinner.setAdapter(dataAdapter);
        pictureSelectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                numberOfFacesTextView.setText("");

                String selectedItem = parent.getItemAtPosition(position).toString();

                switch (selectedItem) {
                    case "picture":
                        pictureImageView.setImageResource(R.drawable.picture);
                        break;
                    case "picture1":
                        pictureImageView.setImageResource(R.drawable.picture1);
                        break;
                    case "picture2":
                        pictureImageView.setImageResource(R.drawable.picture2);
                        break;
                    case "picture3":
                        pictureImageView.setImageResource(R.drawable.picture3);
                        break;
                    case "picture4":
                        pictureImageView.setImageResource(R.drawable.picture4);
                        break;
                    case "picture5":
                        pictureImageView.setImageResource(R.drawable.picture5);
                        break;
                    case "picture6":
                        pictureImageView.setImageResource(R.drawable.picture6);
                        break;
                    case "picture7":
                        pictureImageView.setImageResource(R.drawable.picture7);
                        break;
                    case "picture8":
                        pictureImageView.setImageResource(R.drawable.picture8);
                        break;
                    default:
                        pictureImageView.setImageResource(R.drawable.raw_image);
                }
            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        detectFacesButton = findViewById(R.id.detectFacesButton);
        detectFacesButton.setOnClickListener(this);

        numberOfFacesTextView = findViewById(R.id.numberOfFacesTextView);


    }

    /**
     * This method is called whenever any widget is clicked
     * @param view to het the shown view
     */
    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View view) {
        if (view == detectFacesButton) {

            /* Apply property to bitmap*/
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inMutable = true;

            /* Set the bitmap with the image resource*/
            Bitmap myBitmap = ((BitmapDrawable) pictureImageView.getDrawable()).getBitmap();

            /* using paint to draw rectanlge around image*/
            Paint myRectPaint = new Paint();
            myRectPaint.setStrokeWidth(5);
            myRectPaint.setColor(Color.GREEN);
            myRectPaint.setStyle(Paint.Style.STROKE);

            /* draw the bitmap */
            Bitmap tempBitmap = Bitmap.createBitmap(myBitmap.getWidth(), myBitmap.getHeight(), Bitmap.Config.RGB_565);
            Canvas tempCanvas = new Canvas(tempBitmap);
            tempCanvas.drawBitmap(myBitmap, 0, 0, null);

            /* Applying face detection class*/
            FaceDetector faceDetector = new
                    FaceDetector.Builder(getApplicationContext()).setTrackingEnabled(false)
                    .build();
            if (!faceDetector.isOperational()) {
                /* if there is any error */
                new AlertDialog.Builder(view.getContext()).setMessage("Could not set up the face detector!").show();
                return;
            }

            /* save all the faces in the array */
            Frame frame = new Frame.Builder().setBitmap(myBitmap).build();
            SparseArray<Face> faces = faceDetector.detect(frame);

            /* Draw the rectangle on each face*/
            for (int i = 0; i < faces.size(); i++) {
                Face thisFace = faces.valueAt(i);
                float x1 = thisFace.getPosition().x;
                float y1 = thisFace.getPosition().y;
                float x2 = x1 + thisFace.getWidth();
                float y2 = y1 + thisFace.getHeight();
                tempCanvas.drawRoundRect(new RectF(x1, y1, x2, y2), 2, 2, myRectPaint);
            }
            /* set the imageview with the drawable*/
            pictureImageView.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));

            /* show number of faces */
            numberOfFacesTextView.setText("Number of Faces: " + faces.size());
        }
    }

    /**
     * This method is called whenever the physical back button on device is clicked
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        startActivity(new Intent(PreloadedImagesDetectionActivity.this,MainActivity.class));
    }
}
