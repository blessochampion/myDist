package mydist.mydist.listeners;

import mydist.mydist.models.AuthenticationResponse;
import mydist.mydist.models.DownloadMastersResponse;

/**
 * Created by Blessing.Ekundayo on 11/9/2017.
 */

public interface DownloadMastersListener
{
    void onStartDownload();
    void onSuccess(DownloadMastersResponse response);
    void onFailure();

}
