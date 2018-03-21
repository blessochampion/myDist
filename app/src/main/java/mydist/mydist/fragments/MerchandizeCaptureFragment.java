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
import android.support.annotation.Nullable;
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
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import mydist.mydist.AppController;
import mydist.mydist.R;
import mydist.mydist.activities.StockCountReviewActivity;
import mydist.mydist.adapters.MerchandizeImageAdapter;
import mydist.mydist.adapters.MerchandizingAdapter;
import mydist.mydist.data.DatabaseManager;
import mydist.mydist.data.MasterContract;
import mydist.mydist.data.UserPreference;
import mydist.mydist.utils.Days;
import mydist.mydist.utils.FontManager;
import mydist.mydist.utils.UploadState;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class MerchandizeCaptureFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener, UploadCallback {
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
    HashMap<String, String> imageUrlMap = new HashMap<>();
    String retailerName;
    Map<String, Integer> progressViews = new HashMap<>();
    MerchandizeImageAdapter adapter;

    public MerchandizeCaptureFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MediaManager.get().registerCallback(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        retailerName = UserPreference.getInstance(getActivity()).getUsername();
    }

    private void bindView(Cursor data) {
        if (data.getCount() > 0) {
            mMessage.setVisibility(View.GONE);
            mGridView.setVisibility(View.VISIBLE);
            adapter = new MerchandizeImageAdapter(getActivity(),
                    data,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String action = v.getTag().toString().substring(0, 1);
                            String value = v.getTag().toString().substring(2);
                            if (action.equals(MerchandizeImageAdapter.ACTION_DELETE)) {
                                new DeleteImageTask().execute(value);
                            } else if (action.equals(MerchandizeImageAdapter.ACTION_UPLOAD)) {
                                String publicName = getFileName(value);
                                String requestId = MediaManager.get().upload(Uri.parse(value))
                                        .unsigned("route_images")
                                        .option("public_id", publicName)
                                        .callback(MerchandizeCaptureFragment.this).dispatch();
                                imageUrlMap.put(requestId, value);
                                int position = getPosition(requestId);
                                setUploadProgress(getItemView(position), 0);
                            }
                        }
                    }, progressViews);
            mGridView.setAdapter(adapter);
        } else {
            mGridView.setVisibility(View.GONE);
            mMessage.setVisibility(View.VISIBLE);
        }
    }

    private String getFileName(String value) {
        String result = value.substring(value.lastIndexOf("/") + 1, value.length());
        return result.substring(0, result.lastIndexOf("."));
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
        return new CursorLoader(getActivity()) {
            @Override
            public Cursor loadInBackground() {
                return DatabaseManager.getInstance(getActivity()).getMerchandizeImageUrls(retailerId, Days.getTodayDate());
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (loader.getId() == LOADER_ID) {
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
            new ImageSaveTask().execute(photoUri.toString(), String.valueOf(UploadState.NOT_STARTED));
        }
    }

    @Override
    public void onStart(String requestId) {
        Log.e("onstart", ":::::::" + requestId);
        Toast.makeText(AppController.getInstance(), "Starting", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProgress(String requestId, long bytes, long totalBytes) {
        Log.e("onProgress", ":::::::" + requestId);
        if (imageUrlMap.containsKey(requestId)) {
            int position = getPosition(requestId);
            View view = getItemView(position);
            if (view != null) {
                setUploadProgress(view, ((double) bytes) / totalBytes * 100);
            }
        }
    }

    public static void setUploadProgress(View view, double value) {
        LinearLayout btnContainer = view.findViewById(R.id.ll_btn_container);
        btnContainer.setVisibility(View.GONE);
        ProgressBar progressBar = view.findViewById(R.id.pb_upload_indicator);
        progressBar.setProgress((int) value);
        progressBar.setVisibility(View.VISIBLE);
    }

    public static void setSuccess(View view) {
        LinearLayout btnContainer = view.findViewById(R.id.ll_btn_container);
        btnContainer.setVisibility(View.GONE);
        ProgressBar progressBar = view.findViewById(R.id.pb_upload_indicator);
        progressBar.setVisibility(View.GONE);
        TextView successMessageView = view.findViewById(R.id.tv_upload_success);
        successMessageView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSuccess(String requestId, Map resultData) {
        if (imageUrlMap.containsKey(requestId)) {
            Log.e("OnSuccess", MediaManager.get().url().generate(getFileName(imageUrlMap.get(requestId))));
            Toast.makeText(AppController.getInstance(), "SUCCESS", Toast.LENGTH_SHORT).show();
            if (imageUrlMap.containsKey(requestId)) {
                int position = getPosition(requestId);
                View view = getItemView(position);
                if (view != null) {
                    setSuccess(view);
                }
                new UploadStateSaveTask().execute(imageUrlMap.get(requestId), MediaManager.get().url().generate(getFileName(imageUrlMap.get(requestId)) + JPEG_FORMAT), requestId);
            }
        }
    }

    private int getPosition(String requestId) {
        return progressViews.get(imageUrlMap.get(requestId));
    }

    private View getItemView(int position) {
        int first = mGridView.getFirstVisiblePosition();
        int last = mGridView.getLastVisiblePosition();
        View view = null;
        if (first <= position && position <= last) {
            view = mGridView.getChildAt(position);
        }
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        MediaManager.get().unregisterCallback(this);
        new SaveRequestIdFileOnDiskMap(retailerId).execute(imageUrlMap);

    }

    private void setFailure(View view) {
        LinearLayout btnContainer = view.findViewById(R.id.ll_btn_container);
        btnContainer.setVisibility(View.VISIBLE);
        ProgressBar progressBar = view.findViewById(R.id.pb_upload_indicator);
        progressBar.setVisibility(View.GONE);
        TextView successMessageView = view.findViewById(R.id.tv_upload_success);
        successMessageView.setVisibility(View.GONE);
    }

    @Override
    public void onError(String requestId, ErrorInfo error) {
        if (imageUrlMap.containsKey(requestId)) {
            int position = getPosition(requestId);
            View view = getItemView(position);
            if (view != null) {
                setFailure(view);
            }
        }
    }

    @Override
    public void onReschedule(String requestId, ErrorInfo error) {

    }

    class ImageSaveTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... strings) {
            return DatabaseManager.getInstance(getActivity()).persistMerchandizeImage(retailerId, Days.getTodayDate(), strings[0], strings[1]);
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

    class UploadStateSaveTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... strings) {
            return DatabaseManager.getInstance(getActivity()).updateImageUploadState(retailerId, strings[0], String.valueOf(UploadState.COMPLETED), strings[1], strings[2]);
        }

        @Override
        protected void onPostExecute(Boolean data) {
            if (data) {
               // Toast.makeText(getActivity(), getString(R.string.image_saved_successfully), Toast.LENGTH_LONG).show();
                getLoaderManager().restartLoader(LOADER_ID, null, MerchandizeCaptureFragment.this);
            } else {
                //Toast.makeText(getActivity(), getString(R.string.image_not_saved), Toast.LENGTH_LONG).show();
            }
        }
    }

     class DeleteImageTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... strings) {
            return DatabaseManager.getInstance(getActivity()).removeImage(retailerId, Days.getTodayDate(), strings[0]);
        }

        @Override
        protected void onPostExecute(Boolean data) {
            super.onPostExecute(data);
            if (data) {
                Toast.makeText(getActivity(), getString(R.string.image_saved_successfully), Toast.LENGTH_LONG).show();
                getLoaderManager().restartLoader(LOADER_ID, null, MerchandizeCaptureFragment.this);
            } else {
                Toast.makeText(getActivity(), getString(R.string.unable_to_delete_image), Toast.LENGTH_LONG).show();
            }
        }
    }

   static class SaveRequestIdFileOnDiskMap extends AsyncTask<HashMap<String, String>, Void, Void> {
        String retailerId;

        SaveRequestIdFileOnDiskMap(String retailerId) {
            this.retailerId = retailerId;
        }

        @Override
        protected Void doInBackground(HashMap<String, String>[] hashMaps) {
            HashMap<String, String> maps = hashMaps[0];
            for (String key : maps.keySet()) {
                DatabaseManager.getInstance(AppController.getInstance()).updateImageUploadState(retailerId, maps.get(key), key);
            }
            return null;
        }
    }

}
