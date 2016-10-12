package ru.romanblack.test.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.io.IOException;
import java.util.List;

import ru.romanblack.test.R;
import ru.romanblack.test.ui.activity.ViewPhotoActivity;
import ru.romanblack.test.util.Consts;

public class ScalingFragment extends MainActivityFragment {

    private View galleryButton;
    private View cameraButton;

    private Uri photoUri;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_scaling, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeUi(view);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case Consts.REQUEST_CODE_TAKE_PICTURE:
                if (resultCode == Activity.RESULT_OK) {
                    startViewActivity();
                }
                break;
            case Consts.REQUEST_CODE_PICK_PHOTO:
                if (resultCode == Activity.RESULT_OK) {
                    photoUri = data.getData();

                    startViewActivity();
                }
                break;
        }
    }

    private void initializeUi(View view) {
        galleryButton = view.findViewById(R.id.select);
        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGalleryClicked();
            }
        });

        cameraButton = view.findViewById(R.id.camera);
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCameraClicked();
            }
        });
    }

    private void onGalleryClicked() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, Consts.REQUEST_CODE_PICK_PHOTO);
    }

    private void onCameraClicked() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {

            }

            if (photoFile != null) {
                photoUri = FileProvider.getUriForFile(getContext(),
                        "ru.romanblack.test.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);

                List<ResolveInfo> resInfoList = getContext()
                        .getPackageManager()
                        .queryIntentActivities(
                                takePictureIntent,
                                PackageManager.MATCH_DEFAULT_ONLY
                        );
                for (ResolveInfo resolveInfo : resInfoList) {
                    String packageName = resolveInfo.activityInfo.packageName;
                    getContext().grantUriPermission(
                            packageName,
                            photoUri,
                            Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                                    | Intent.FLAG_GRANT_READ_URI_PERMISSION
                    );
                }

                startActivityForResult(takePictureIntent, Consts.REQUEST_CODE_TAKE_PICTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        String imageFileName = "img";
        File storageDir = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        return image;
    }

    private void startViewActivity() {
        Intent intent = new Intent(getContext(), ViewPhotoActivity.class);
        intent.putExtra(Consts.EXTRA_FILEPATH, photoUri.toString());
        startActivity(intent);
    }
}
