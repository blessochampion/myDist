package mydist.mydist.listeners;

import mydist.mydist.models.DownloadMastersResponse;
import mydist.mydist.models.UploadMastersResponse;

/**
 * Created by Blessing.Ekundayo on 11/9/2017.
 */

public interface UploadMastersListener
{
    void onStartUpload();
    void onSuccess(UploadMastersResponse response);
    void onFailure();

}
