package mydist.mydist.adapters;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import mydist.mydist.R;
import mydist.mydist.data.MasterContract;
import mydist.mydist.fragments.MerchandizeCaptureFragment;
import mydist.mydist.utils.UploadState;

/**
 * Created by Blessing.Ekundayo on 3/13/2018.
 */

public class MerchandizeImageAdapter extends CursorAdapter {
    public static final String ACTION_DELETE = "1";
    public static final String ACTION_UPLOAD = "0";
    public static final String DELIMITER = ":";
    Map<String, Integer> viewMap;
    Context context;
    View.OnClickListener listener;

    public MerchandizeImageAdapter(Context context, Cursor c, View.OnClickListener listener, Map<String,Integer> viewMap) {
        super(context, c, true);
        this.context = context;
        this.listener = listener;
        this.viewMap = viewMap;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(R.layout.merchandize_image_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String imageUrl = cursor.getString(cursor.getColumnIndex(MasterContract.MerchandizeImageContract.IMAGE_URI_ON_DISK));
        String uploadState = cursor.getString(cursor.getColumnIndex(MasterContract.MerchandizeImageContract.UPLOAD_STATE));
        Button deleteButton = (Button) view.findViewById(R.id.btn_delete);
        Button uploadButton = (Button) view.findViewById(R.id.btn_upload);
        String tag = ACTION_DELETE + DELIMITER + imageUrl;
        deleteButton.setTag(tag);
        deleteButton.setOnClickListener(listener);
        tag = ACTION_UPLOAD + DELIMITER + imageUrl;
        uploadButton.setTag(tag);
        uploadButton.setOnClickListener(listener);
        viewMap.put(imageUrl,cursor.getPosition());
        ImageView imageView = (ImageView) view.findViewById(R.id.iv_merchandize_image);
        Picasso.with(context).load(imageUrl).noFade().into(imageView);
        if(uploadState.equals(UploadState.COMPLETED.toString())){
            MerchandizeCaptureFragment.setSuccess(view);
        }
    }
}
