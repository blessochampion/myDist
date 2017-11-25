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
import mydist.mydist.models.AuthenticationResponse;

import static com.android.volley.Request.Method.GET;

/**
 * Created by Blessing.Ekundayo on 11/9/2017.
 */

public class AuthenticationClient implements Response.ErrorListener, Response.Listener<JSONObject> {
    AuthenticationListener authenticationListener;
    String TAG = AuthenticationClient.class.getSimpleName();

    public void authenticate(String username, String password, AuthenticationListener listener) {
        authenticationListener = listener;
        authenticationListener.onStartAuthentication();
        String url = NetworkUtils.getAuthenticationURL(username, password);
        JsonObjectRequest request = new JsonObjectRequest(GET, url, null, this, this);
        AppController.getInstance().addToRequestQueue(request);

    }

    @Override
    public void onErrorResponse(VolleyError error) {
        authenticationListener.onFailure();
    }

    @Override
    public void onResponse(JSONObject response) {
        AuthenticationResponse authResponse = parseResponse(response);
        if (authResponse.getUser() != null && authResponse.getStatus() != null
                && authResponse.getStatus().isSuccess()) {
            authenticationListener.onSuccess(authResponse);
        } else {
            authenticationListener.onFailure();
        }
    }

    private AuthenticationResponse parseResponse(JSONObject response) {
        AuthenticationResponse authenticationResponse = null;
        final ObjectMapper mapper = new ObjectMapper();
        try {
            authenticationResponse = mapper.readValue(response.toString(), AuthenticationResponse.class);
        } catch (IOException ioe) {
            Log.e(TAG, ioe.getMessage());
        }

        return authenticationResponse;
    }
}
