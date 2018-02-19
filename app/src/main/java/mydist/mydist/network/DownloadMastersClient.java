package mydist.mydist.network;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONObject;

import java.io.IOException;

import mydist.mydist.AppController;
import mydist.mydist.listeners.AuthenticationListener;
import mydist.mydist.listeners.DownloadMastersListener;
import mydist.mydist.models.AuthenticationResponse;
import mydist.mydist.models.DownloadMastersResponse;

import static com.android.volley.Request.Method.GET;

/**
 * Created by Blessing.Ekundayo on 11/9/2017.
 */

public class DownloadMastersClient implements Response.ErrorListener, Response.Listener<JSONObject> {
    DownloadMastersListener downloadMastersListener;
    String TAG = DownloadMastersClient.class.getSimpleName();

    public void downloadMasters(String username, String password, DownloadMastersListener listener) {
        downloadMastersListener = listener;
        downloadMastersListener.onStartDownload();
        String url = NetworkUtils.getMastersDownloadURL(username, password);
        JsonObjectRequest request = new JsonObjectRequest(GET, url, null, this, this);
        AppController.getInstance().addToRequestQueue(request);

    }

    @Override
    public void onErrorResponse(VolleyError error) {
        downloadMastersListener.onFailure();
    }

    @Override
    public void onResponse(JSONObject response) {
        DownloadMastersResponse downloadResponse = parseResponse(response);
        if (downloadResponse.getStatus() != null && downloadResponse.getStatus().isSuccess()) {
            downloadMastersListener.onSuccess(downloadResponse);
        } else {
            downloadMastersListener.onFailure();
        }
    }

    private DownloadMastersResponse parseResponse(JSONObject response) {
        DownloadMastersResponse downloadMastersResponse = null;
        final ObjectMapper mapper = new ObjectMapper();
        try {
            downloadMastersResponse = mapper.readValue(response.toString(), DownloadMastersResponse.class);
        } catch (IOException ioe) {
            Log.e(TAG, ioe.getMessage());
        }
        return downloadMastersResponse;
    }
}
