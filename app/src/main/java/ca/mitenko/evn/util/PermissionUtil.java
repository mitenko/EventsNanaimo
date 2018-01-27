package ca.mitenko.evn.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

/**
 * Created by mitenko on 2017-08-19.
 */

public class PermissionUtil {
    /**
     * REturns true / false if the user has granted the FINE_LOCATION
     * @return
     */
    public static boolean hasFineLocationPermission(Context context) {
        String permission = "android.permission.ACCESS_FINE_LOCATION";
        int res = ContextCompat.checkSelfPermission(context, permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }
}
