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

import mydist.mydist.R;
import mydist.mydist.data.MasterContract;

/**
 * Created by Blessing.Ekundayo on 3/13/2018.
 */

public class MerchandizeImageAdapter extends CursorAdapter {
    public static final String ACTION_DELETE = "1";
    public static final String ACTION_UPLOAD = "0";
    public static final String DELIMITER = ":";
    Context context;
    View.OnClickListener listener;

    public MerchandizeImageAdapter(Context context, Cursor c, View.OnClickListener listener) {
        super(context, c, true);
        this.context = context;
        this.listener = listener;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(R.layout.merchandize_image_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String imageUrl = cursor.getString(cursor.getColumnIndex(MasterContract.MerchandizeImageContract.IMAGE_URI_ON_DISK));
        Button deleteButton = (Button) view.findViewById(R.id.btn_delete);
        String tag = ACTION_DELETE + DELIMITER + imageUrl;
        Log.e("dddd",imageUrl);
        deleteButton.setTag(tag);
        deleteButton.setOnClickListener(listener);
        ImageView imageView = (ImageView) view.findViewById(R.id.iv_merchandize_image);
        Picasso.with(context).load(imageUrl).into(imageView);
    }
}
