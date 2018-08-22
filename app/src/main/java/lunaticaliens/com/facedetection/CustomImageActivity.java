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
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class CustomImageActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView pictureImageView;
    Button detectFacesButton;
    TextView hintTextView, numberOfFacesTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_image);

        pictureImageView = findViewById(R.id.pictureImageView);
        detectFacesButton = findViewById(R.id.detectFaces);

        pictureImageView.setOnClickListener(this);
        detectFacesButton.setOnClickListener(this);

        hintTextView = findViewById(R.id.hintTextView);
        numberOfFacesTextView = findViewById(R.id.numberOfFacesTextView);
        numberOfFacesTextView.setVisibility(View.INVISIBLE);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View view) {

        if(view == pictureImageView){
            hintTextView.setVisibility(View.INVISIBLE);
            // start picker to get image for cropping and then use the image in cropping activity
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(this);
        }

        if(view == detectFacesButton){
            /* Apply property to bitmap*/
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inMutable = true;

            /* Set the bitmap with the image resource*/
            Bitmap myBitmap = ((BitmapDrawable) pictureImageView.getDrawable()).getBitmap();

            /* using paint to draw rectangle around image*/
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

            if(faces.size() == 0){
                Toast.makeText(this, "No face was detected", Toast.LENGTH_SHORT).show();
            }

            /* show number of faces */
            numberOfFacesTextView.setText("Number of Faces: " + faces.size());
            numberOfFacesTextView.setVisibility(View.VISIBLE);
        }
    }

    /**
     *
     * This method is called when an image is cropped successfully.
     * @param requestCode the code send for cropping
     * @param resultCode the code received for cropping
     * @param data the data of the attributes of the crop activity
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                pictureImageView.setImageURI(resultUri);
            }
        }
    }

    /**
     * This method is called whenever the physical back button on device is clicked
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        startActivity(new Intent(CustomImageActivity.this,MainActivity.class));
    }
}
