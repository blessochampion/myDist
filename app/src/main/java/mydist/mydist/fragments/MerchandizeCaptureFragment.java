package mydist.mydist.fragments;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import mydist.mydist.R;
import mydist.mydist.activities.SynchronizationActivity;
import mydist.mydist.utils.Days;
import mydist.mydist.utils.FontManager;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class MerchandizeCaptureFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {
    TextView mMessage;
    FrameLayout container;
    View contentView;
    ImageView imageView;
    private static final String KEY_RETAILER_ID = "retailer_id";
    private static final int LOADER_ID = 70974;
    public static final int CAMERA_USE_REQUEST_CODE = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 3000;
    String retailerId;
    LinearLayout captureButton;

    public MerchandizeCaptureFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        contentView = inflater.inflate(R.layout.fragment_merchandize_capture, container, false);
        getReferencesToWidgets();
        retailerId = getArguments().getString(KEY_RETAILER_ID);
        setFonts();
        getLoaderManager().initLoader(LOADER_ID, null, this);
        return contentView;
    }

    private void setFonts() {
        Typeface ralewayFont = FontManager.getTypeface(getActivity(), FontManager.RALEWAY_REGULAR);
        FontManager.setFontsForView(contentView.findViewById(R.id.parent_layout), ralewayFont);
        setIcon();
    }

    public void getReferencesToWidgets() {
        container = (FrameLayout) contentView.findViewById(R.id.container);
        captureButton = (LinearLayout) contentView.findViewById(R.id.ll_capture_merchandize);
        captureButton.setOnClickListener(this);
        mMessage = (TextView) contentView.findViewById(R.id.tv_message);
        imageView = (ImageView) contentView.findViewById(R.id.image);
    }

    private void setIcon() {
        Typeface fontAwesome = FontManager.getTypeface(getActivity(), FontManager.FONT_AWESOME);
        FontManager.setFontsForView(contentView.findViewById(R.id.icon_capture_merchandize), fontAwesome);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public static MerchandizeCaptureFragment getNewInstance(String retailerId) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_RETAILER_ID, retailerId);
        MerchandizeCaptureFragment merchandizeCaptureFragment = new MerchandizeCaptureFragment();
        merchandizeCaptureFragment.setArguments(bundle);
        return merchandizeCaptureFragment;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ll_capture_merchandize) {
            if (cameraIsPermitted()) {
                launchCamera();
            } else {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_USE_REQUEST_CODE);
            }
        }
    }

    private boolean cameraIsPermitted() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_USE_REQUEST_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                launchCamera();
            } else {
                AlertDialog dialog = new AlertDialog.Builder(getActivity()).
                        setMessage(this.getString(R.string.camera_message)).
                        setPositiveButton(this.getString(R.string.ok), null).create();
                dialog.show();
            }
        }
    }

    private void launchCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
            mMessage.setText("");
        }
    }
}
