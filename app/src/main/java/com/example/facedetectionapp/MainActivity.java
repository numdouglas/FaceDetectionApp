package com.example.facedetectionapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.mlkit.vision.common.InputImage;
import com.camerakit.CameraKitView;
import com.example.facedetectionapp.Helper.GraphicOverlay;
import com.example.facedetectionapp.Helper.RectOverlay;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;

import java.util.List;

import dmax.dialog.SpotsDialog;

public class MainActivity extends AppCompatActivity {

    private Button faceDetectButton;
    private GraphicOverlay graphicOverlay;
    private CameraKitView cameraKitView;
    public AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        faceDetectButton=findViewById(R.id.detect_face);
        graphicOverlay=findViewById(R.id.graphic_overlay);
        cameraKitView=findViewById(R.id.camera_view);
        alertDialog=new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Detecting Faces...")
                .setCancelable(false)
                .build();

        faceDetectButton.setOnClickListener(v -> {
            cameraKitView.captureImage((cameraKitView, capturedImage) -> {
                cameraKitView.onStop();
                alertDialog.show();
                Bitmap bitmap= BitmapFactory.decodeByteArray(capturedImage,0,capturedImage.length);
                bitmap=Bitmap.createScaledBitmap(bitmap,cameraKitView.getWidth(),
                        cameraKitView.getHeight(),false);

                processFaceDetection(bitmap);
            });
       graphicOverlay.clear();
        });

    }

    private void processFaceDetection(Bitmap bitmap) {

        InputImage firebaseVisionImage=InputImage.fromBitmap(bitmap,0);
        FaceDetectorOptions faceDetectorOptions=
                new FaceDetectorOptions.Builder().build();

        FaceDetector firebaseVisionFaceDetector= FaceDetection.getClient(faceDetectorOptions);
        firebaseVisionFaceDetector.process(firebaseVisionImage).addOnSuccessListener(
                this::getFaceResults
        ).addOnFailureListener(e -> Toast.makeText(MainActivity.this,"Error :"+e.getMessage(),Toast.LENGTH_SHORT).show());

    }

    private void getFaceResults(List<Face> firebaseVisionFaces) {

        for(Face face:firebaseVisionFaces){
            Rect bounds=face.getBoundingBox();
            RectOverlay rectOverlay=new RectOverlay(graphicOverlay,bounds);

            graphicOverlay.add(rectOverlay);
        }
        alertDialog.dismiss();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        cameraKitView.onRequestPermissionsResult(requestCode,permissions,grantResults);
    }


    @Override
    protected void onPause() {
        super.onPause();
        cameraKitView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraKitView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        cameraKitView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        cameraKitView.onStop();
    }
}