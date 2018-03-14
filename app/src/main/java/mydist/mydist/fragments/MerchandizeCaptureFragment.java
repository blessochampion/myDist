package mydist.mydist.fragments;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.FileProvider;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import mydist.mydist.R;
import mydist.mydist.activities.StockCountReviewActivity;
import mydist.mydist.adapters.MerchandizeImageAdapter;
import mydist.mydist.adapters.MerchandizingAdapter;
import mydist.mydist.data.DatabaseManager;
import mydist.mydist.data.MasterContract;
import mydist.mydist.data.UserPreference;
import mydist.mydist.utils.Days;
import mydist.mydist.utils.FontManager;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class MerchandizeCaptureFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {
    public static final String JPEG_FORMAT = ".jpeg";
    public static final String UNDERSCORE_DELIMITER = "_";
    TextView mMessage;
    FrameLayout container;
    View contentView;
    GridView mGridView;
    private static final String KEY_RETAILER_ID = "retailer_id";
    private static final int LOADER_ID = 70974;
    public static final int CAMERA_USE_REQUEST_CODE = 1;
    public static final int WRITE_PERMISSION_REQUEST_CODE = 2;
    private static final int REQUEST_IMAGE_CAPTURE = 3000;
    String retailerId;
    LinearLayout captureButton;
    File currentPhotoFile;
    Intent takePictureIntent;
    Uri photoUri;

    public MerchandizeCaptureFragment() {
        // Required empty public constructor
    }


    private void bindView(Cursor data)
    {
        if(data.getCount() >0){
            mMessage.setVisibility(View.GONE);
            mGridView.setVisibility(View.VISIBLE);
            mGridView.setAdapter(new MerchandizeImageAdapter(getActivity(), data, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String  action = v.getTag().toString().substring(0,1);
                    String value = v.getTag().toString().substring(2);
                    if(action.equals(MerchandizeImageAdapter.ACTION_DELETE)){
                        Log.e("ddd", value);
                        new DeleteImageTask().execute(value);
                    }else {

                    }
                }
            }));
        }else {
            mGridView.setVisibility(View.GONE);
            mMessage.setVisibility(View.VISIBLE);
        }
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
        mGridView = (GridView) contentView.findViewById(R.id.gv_images);
    }

    private void setIcon() {
        Typeface fontAwesome = FontManager.getTypeface(getActivity(), FontManager.FONT_AWESOME);
        FontManager.setFontsForView(contentView.findViewById(R.id.icon_capture_merchandize), fontAwesome);
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity()){
            @Override
            public Cursor loadInBackground() {
                return DatabaseManager.getInstance(getActivity()).getMerchandizeImageUrls(retailerId, Days.getTodayDate());
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(loader.getId() == LOADER_ID){
            bindView(data);
        }
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
        } else if (requestCode == WRITE_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                goToCameraActivity();
            } else {
                AlertDialog dialog = new AlertDialog.Builder(getActivity()).
                        setMessage(this.getString(R.string.camera_message)).
                        setPositiveButton(this.getString(R.string.ok), null).create();
                dialog.show();
            }
        }
    }

    private void launchCamera() {
        takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            if (writePermissionIsGranted()) {
                goToCameraActivity();
            } else {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_PERMISSION_REQUEST_CODE);
            }
        }
    }

    private void goToCameraActivity() {
        try {
            currentPhotoFile = createImageFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (currentPhotoFile != null) {
             photoUri = FileProvider.getUriForFile(getActivity(), "mydist.mydist.fileprovider", currentPhotoFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } else {
            Toast.makeText(getActivity(), getString(R.string.camera_error), Toast.LENGTH_LONG).show();
        }

    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        String fileName = UserPreference.getInstance(getActivity()).getUsername() + UNDERSCORE_DELIMITER + retailerId +
                UNDERSCORE_DELIMITER + timeStamp;
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        storageDir = new File(storageDir, "route_Images");
        if (!storageDir.exists()) {
            storageDir.mkdir();
        }
        return File.createTempFile(fileName, JPEG_FORMAT, storageDir);

    }

    private boolean cameraIsPermitted() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }

    private boolean writePermissionIsGranted() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
         new ImageSaveTask().execute(photoUri.toString());
        }
    }

    class ImageSaveTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... strings) {
            return DatabaseManager.getInstance(getActivity()).persistMerchandizeImage(retailerId, Days.getTodayDate(), strings[0]);
        }

        @Override
        protected void onPostExecute(Boolean data) {
            if (data) {
                Toast.makeText(getActivity(), getString(R.string.image_saved_successfully), Toast.LENGTH_LONG).show();
                getLoaderManager().restartLoader(LOADER_ID, null, MerchandizeCaptureFragment.this);
            } else {
                Toast.makeText(getActivity(), getString(R.string.image_not_saved), Toast.LENGTH_LONG).show();
            }
        }
    }

    class DeleteImageTask extends AsyncTask<String , Void , Boolean>{

        @Override
        protected Boolean doInBackground(String... strings) {
            return  DatabaseManager.getInstance(getActivity()).removeImage(retailerId, Days.getTodayDate(), strings[0]);
        }

        @Override
        protected void onPostExecute(Boolean data) {
            super.onPostExecute(data);
            if(data){
                Toast.makeText(getActivity(), getString(R.string.image_saved_successfully), Toast.LENGTH_LONG).show();
                getLoaderManager().restartLoader(LOADER_ID, null, MerchandizeCaptureFragment.this);
            }else {
                Toast.makeText(getActivity(), getString(R.string.unable_to_delete_image), Toast.LENGTH_LONG).show();
            }
        }
    }
}
