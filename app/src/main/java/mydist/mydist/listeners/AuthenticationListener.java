package mydist.mydist.listeners;

import mydist.mydist.models.AuthenticationResponse;

/**
 * Created by Blessing.Ekundayo on 11/9/2017.
 */

public interface AuthenticationListener
{
    void onStartAuthentication();
    void onSuccess(AuthenticationResponse response);
    void onFailure();

}
