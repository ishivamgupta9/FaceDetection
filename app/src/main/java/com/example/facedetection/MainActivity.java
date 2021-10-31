package com.example.facedetection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.example.facedetection.helper.GraphicOverlay;
import com.example.facedetection.helper.RectOverlay;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions;
import com.wonderkiln.camerakit.CameraKitError;
import com.wonderkiln.camerakit.CameraKitEvent;
import com.wonderkiln.camerakit.CameraKitEventListenerAdapter;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraKitVideo;
import com.wonderkiln.camerakit.CameraView;

import java.util.List;

import dmax.dialog.SpotsDialog;

public class MainActivity extends AppCompatActivity {


    Button faceDetectButton;
    GraphicOverlay graphicOverlay;
    CameraView cameraView;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

if(getSupportActionBar()!=null)
    this.getSupportActionBar().hide();



        faceDetectButton=findViewById(R.id.detect_face_btn);
        graphicOverlay=findViewById(R.id.graphic_overlay);
     cameraView=findViewById(R.id.camera_view);


        alertDialog=new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Loading")
                .setCancelable(false)
                .build();


    faceDetectButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
         cameraView.start();
         cameraView.captureImage();
         graphicOverlay.clear();

        }
    });
cameraView.addCameraKitListener(new CameraKitEventListenerAdapter() {
    @Override
    public void onEvent(CameraKitEvent event) {
        super.onEvent(event);

    }

    @Override
    public void onError(CameraKitError error) {
        super.onError(error);
    }

    @Override
    public void onImage(CameraKitImage image) {
        super.onImage(image);
        alertDialog.show();
        Bitmap bitmap=image.getBitmap();
        bitmap=Bitmap.createScaledBitmap(bitmap,cameraView.getWidth(),cameraView.getHeight(),false);
        cameraView.stop();


        processFacedDetection(bitmap);
    }

    @Override
    public void onVideo(CameraKitVideo video) {
        super.onVideo(video);
    }
});
    }

    private void processFacedDetection(Bitmap bitmap) {

        FirebaseVisionImage firebaseVisionImage=FirebaseVisionImage.fromBitmap(bitmap);
        FirebaseVisionFaceDetectorOptions firebaseVisionFaceDetectorOptions=new FirebaseVisionFaceDetectorOptions.Builder().build();
        FirebaseVisionFaceDetector firebaseVisionFaceDetector= FirebaseVision.getInstance().getVisionFaceDetector(firebaseVisionFaceDetectorOptions);

        firebaseVisionFaceDetector.detectInImage(firebaseVisionImage).addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionFace>>() {
            @Override
            public void onSuccess(List<FirebaseVisionFace> firebaseVisionFaces) {

            getFaceResult(firebaseVisionFaces);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this,"Error"+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void getFaceResult(List<FirebaseVisionFace> firebaseVisionFaces) {

        int c=0;
        for(FirebaseVisionFace face:firebaseVisionFaces)
        {
            Rect rect=face.getBoundingBox();
            RectOverlay rectOverlay=new RectOverlay(graphicOverlay,rect);

            graphicOverlay.add(rectOverlay);
            c=c+1;
        }

        alertDialog.dismiss();
    }

    @Override
    protected void onPause() {
        super.onPause();
cameraView.stop();

    }

    @Override
    protected void onResume() {
        super.onResume();
    cameraView.start();

    }
}