package com.example.sikesal3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.jpegkit.Jpeg;
import com.jpegkit.JpegImageView;

public class CameraHome extends AppCompatActivity implements CameraCapture.OnInputListener {
    Button btn;
    private JpegImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_home);

         btn = findViewById(R.id.button);
        imageView = findViewById(R.id.imageView);
        btn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                CameraCapture dialog = new CameraCapture();
                dialog.show(fm, "fragment_camera");

            }
        });

    }


    @Override
    public void onSimpanClick(Jpeg data) {
        imageView.setJpeg(data);
    }
}
