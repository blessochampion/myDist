package mydist.mydist.utils;

/**
 * Created by Blessing.Ekundayo on 3/18/2018.
 */

public enum UploadState {
    NOT_STARTED (0),
    COMPLETED(1);

    private int state;
     UploadState(int i){
        this.state = i;
    }
    @Override
    public String toString() {
        return String.valueOf(state);
    }
}
