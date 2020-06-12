package com.example.sikesal3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jpegkit.Jpeg;
import com.jpegkit.JpegImageView;

import java.io.File;

public class CameraHome extends AppCompatActivity implements CameraCapture.OnInputListener {
    Button btn;
    private JpegImageView imageView;
    TextView txt_file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_home);

         btn = findViewById(R.id.button);
        imageView = findViewById(R.id.imageView);
        txt_file = findViewById(R.id.textView);
        btn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                CameraCapture dialog = new CameraCapture();
                dialog.show(fm, "fragment_camera");

            }
        });

    }


    @Override
    public void onSimpanClick(Jpeg data, File file) {
        imageView.setJpeg(data);
        txt_file.setText("File = "+file.getName()+"");
        btn.setText("Ambil Ulang Gambar");
        Log.i("file", "onSimpanClick: "+file.getName());
    }
}
