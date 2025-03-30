package com.example.billsplitter.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.billsplitter.R;
import com.example.billsplitter.databinding.ActivityGetPhotoBinding;

import java.io.File;


public class GetPhotoActivity extends AppCompatActivity {

    ActivityGetPhotoBinding mainBinding; // FIXED: Use the correct ViewBinding
    ActivityResultLauncher<Uri> takePictureLauncher;
    Uri imageUri;
    private static final int CAMERA_PERMISSION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // FIXED: Use correct ViewBinding for activity_get_photo.xml
        mainBinding = ActivityGetPhotoBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot()); // FIXED: Set the correct content view

        mainBinding.imageAddNext.setEnabled(false);

        // Adjust window insets properly
        ViewCompat.setOnApplyWindowInsetsListener(mainBinding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize URI for image storage
        imageUri = createUri();
        registerPictureLauncher();

        // FIXED: Correct button reference using the correct binding
        mainBinding.imageAddButton.setOnClickListener(view -> {
            checkCameraPermissionAndOpenCamera();
        });
    }

    private Uri createUri() {
        File imageFile = new File(getApplicationContext().getFilesDir(), "camera_photo.jpg");
        return FileProvider.getUriForFile(
                getApplicationContext(),
                "com.example.billsplitter.fileprovider",
                imageFile
        );
    }

    private void registerPictureLauncher() {
        takePictureLauncher = registerForActivityResult(
                new ActivityResultContracts.TakePicture(),
                new ActivityResultCallback<Boolean>() {
                    @Override
                    public void onActivityResult(Boolean result) {
                        try {
                            if (result) {
                                mainBinding.imageView.setImageURI(null);
                                mainBinding.imageView.setImageURI(imageUri);

                                // Enable another button after taking a picture
                                mainBinding.imageAddNext.setEnabled(true);
                                mainBinding.imageAddNext.setOnClickListener(view -> {
                                    Toast.makeText(GetPhotoActivity.this, "Processing Image...", Toast.LENGTH_SHORT).show();
                                    // Add image processing or other actions here
                                });

                            }
                        } catch (Exception e) {
                            e.getStackTrace();
                        }
                    }
                }
        );
    }

    private void checkCameraPermissionAndOpenCamera() {
        if (ActivityCompat.checkSelfPermission(GetPhotoActivity.this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(GetPhotoActivity.this,
                    new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        }
        else {
            takePictureLauncher.launch(imageUri);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults, int deviceId) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults, deviceId);
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takePictureLauncher.launch(imageUri);
            }
            else {
                Toast.makeText(this, "Camera permission denied, please allow permission to take picture.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
