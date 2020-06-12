package com.example.sikesal3;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.camerakit.CameraKit;
import com.camerakit.CameraKitView;
import com.camerakit.type.CameraSize;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jpegkit.Jpeg;
import com.jpegkit.JpegImageView;

import java.io.File;
import java.io.FileOutputStream;


/**
 * A simple {@link Fragment} subclass.
 */
public class CameraCapture extends DialogFragment  {


    private CameraKitView cameraView;
    private Toolbar toolbar;

    private AppCompatTextView facingText;
    private AppCompatTextView flashText;
    private AppCompatTextView previewSizeText;
    private AppCompatTextView photoSizeText;

    private Button flashOnButton;
    private Button flashOffButton;

    private FloatingActionButton photoButton;

    private Button facingFrontButton;
    private Button facingBackButton;

    private Button permissionsButton;

    private JpegImageView imageView;

    public CameraCapture() {
        // Required empty public constructor
    }

    public interface OnInputListener {
        void onSimpanClick(Jpeg data, File file);
    }
    public OnInputListener onInputListener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_camera_capture, null);


        cameraView = view.findViewById(R.id.camera);

        toolbar = view.findViewById(R.id.toolbar);

        facingText = view.findViewById(R.id.facingText);
        flashText = view.findViewById(R.id.flashText);
        previewSizeText = view.findViewById(R.id.previewSizeText);
        photoSizeText = view.findViewById(R.id.photoSizeText);

        photoButton = view.findViewById(R.id.photoButton);
        photoButton.setOnClickListener(photoOnClickListener);

        flashOnButton = view.findViewById(R.id.flashOnButton);
        flashOffButton = view.findViewById(R.id.flashOffButton);

        flashOnButton.setOnClickListener(flashOnOnClickListener);
        flashOffButton.setOnClickListener(flashOffOnClickListener);

        facingFrontButton = view.findViewById(R.id.facingFrontButton);
        facingBackButton = view.findViewById(R.id.facingBackButton);

        facingFrontButton.setOnClickListener(facingFrontOnClickListener);
        facingBackButton.setOnClickListener(facingBackOnClickListener);

        permissionsButton = view.findViewById(R.id.permissionsButton);
        permissionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraView.requestPermissions(getActivity());
            }
        });

        imageView = view.findViewById(R.id.imageView);

        cameraView.setPermissionsListener(new CameraKitView.PermissionsListener() {
            @Override
            public void onPermissionsSuccess() {
                permissionsButton.setVisibility(View.GONE);
            }

            @Override
            public void onPermissionsFailure() {
                permissionsButton.setVisibility(View.VISIBLE);
            }
        });

        cameraView.setCameraListener(new CameraKitView.CameraListener() {
            @Override
            public void onOpened() {
                Log.v("CameraKitView", "CameraListener: onOpened()");
            }

            @Override
            public void onClosed() {
                Log.v("CameraKitView", "CameraListener: onClosed()");
            }
        });

        cameraView.setPreviewListener(new CameraKitView.PreviewListener() {
            @Override
            public void onStart() {
                Log.v("CameraKitView", "PreviewListener: onStart()");
                updateInfoText();
            }

            @Override
            public void onStop() {
                Log.v("CameraKitView", "PreviewListener: onStop()");
            }
        });
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullscreenDialogTheme);
    }




    @Override
    public void onStart() {
        super.onStart();
        cameraView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        cameraView.onResume();
    }

    @Override
    public void onPause() {
        cameraView.onPause();
        super.onPause();
    }

    @Override
    public void onStop() {
        cameraView.onStop();
        super.onStop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        cameraView.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    private View.OnClickListener photoOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            cameraView.captureImage(new CameraKitView.ImageCallback() {
                @Override
                public void onImage(CameraKitView view, final byte[] photo) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            final Jpeg jpeg = new Jpeg(photo);
                            imageView.post(new Runnable() {
                                @Override
                                public void run() {
                                   // imageView.setJpeg(jpeg);
                                    File savedPhoto = new File(Environment.getExternalStorageDirectory(), "photo.jpg");
                                    try {
                                        FileOutputStream outputStream = new FileOutputStream(savedPhoto.getPath());
                                        outputStream.write(photo);
                                        outputStream.close();
                                    } catch (java.io.IOException e) {
                                        e.printStackTrace();
                                    }
                                    onInputListener.onSimpanClick(jpeg, savedPhoto);
                                    getDialog().cancel();
                                }
                            });
                        }
                    }).start();
                }
            });
        }
    };

    private View.OnClickListener flashOnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (cameraView.getFlash() != CameraKit.FLASH_ON) {
                cameraView.setFlash(CameraKit.FLASH_ON);
                updateInfoText();
            }
        }
    };

    private View.OnClickListener flashOffOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (cameraView.getFlash() != CameraKit.FLASH_OFF) {
                cameraView.setFlash(CameraKit.FLASH_OFF);
                updateInfoText();
            }
        }
    };

    private View.OnClickListener facingFrontOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            cameraView.setFacing(CameraKit.FACING_FRONT);
        }
    };

    private View.OnClickListener facingBackOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            cameraView.setFacing(CameraKit.FACING_BACK);
        }
    };

    private void updateInfoText() {
        String facingValue = cameraView.getFacing() == CameraKit.FACING_BACK ? "BACK" : "FRONT";
        facingText.setText(Html.fromHtml("<b>Facing:</b> " + facingValue));

        String flashValue = "OFF";
        switch (cameraView.getFlash()) {
            case CameraKit.FLASH_OFF: {
                flashValue = "OFF";
                break;
            }

            case CameraKit.FLASH_ON: {
                flashValue = "ON";
                break;
            }

            case CameraKit.FLASH_AUTO: {
                flashValue = "AUTO";
                break;
            }

            case CameraKit.FLASH_TORCH: {
                flashValue = "TORCH";
                break;
            }
        }
        flashText.setText(Html.fromHtml("<b>Flash:</b> " + flashValue));

        CameraSize previewSize = cameraView.getPreviewResolution();
        if (previewSize != null) {
            previewSizeText.setText(Html.fromHtml(String.format("<b>Preview Resolution:</b> %d x %d", previewSize.getWidth(), previewSize.getHeight())));
        }

        CameraSize photoSize = cameraView.getPhotoResolution();
        if (photoSize != null) {
            photoSizeText.setText(Html.fromHtml(String.format("<b>Photo Resolution:</b> %d x %d", photoSize.getWidth(), photoSize.getHeight())));
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onInputListener = (OnInputListener) getActivity();
        } catch (ClassCastException e) {
//            Log.e(TAG, "onAttach: " + e.getMessage());
        }
    }




}
