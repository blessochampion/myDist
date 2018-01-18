package mydist.mydist.network;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONObject;

import java.io.IOException;

import mydist.mydist.AppController;
import mydist.mydist.listeners.UploadMastersListener;
import mydist.mydist.models.UploadMastersResponse;

import static com.android.volley.Request.Method.POST;

/**
 * Created by Blessing.Ekundayo on 11/9/2017.
 */

public class UploadMastersClient implements Response.ErrorListener, Response.Listener<JSONObject> {
    UploadMastersListener upload;
    String TAG = UploadMastersClient.class.getSimpleName();

    public void uploadMasters(JSONObject masters,UploadMastersListener listener) {
        upload = listener;
        upload.onStartUpload();
        String url = NetworkUtils.getUploadURL();
        JsonObjectRequest request = new JsonObjectRequest(POST, url, masters, this, this);
        AppController.getInstance().addToRequestQueue(request);

    }

    @Override
    public void onErrorResponse(VolleyError error) {
        upload.onFailure();
    }

    @Override
    public void onResponse(JSONObject response) {
        UploadMastersResponse downloadResponse = parseResponse(response);
        if (downloadResponse.getStatus() != null && downloadResponse.getStatus().isSuccess()) {
            upload.onSuccess(downloadResponse);
        } else {
            upload.onFailure();
        }
    }

    private UploadMastersResponse parseResponse(JSONObject response) {
        UploadMastersResponse uploadMastersResponse = null;
        final ObjectMapper mapper = new ObjectMapper();
        try {
            uploadMastersResponse = mapper.readValue(response.toString(), UploadMastersResponse.class);
        } catch (IOException ioe) {
            Log.e(TAG, ioe.getMessage());
        }

        return uploadMastersResponse;
    }
}
