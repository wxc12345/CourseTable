package ly.coursetable.demo;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by happts on 2017/8/12.
 */

public class CommonUtil {

    public static ProgressDialog getProcessDialog(Context context, String tips){
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage(tips);
        dialog.setCancelable(false);
        return dialog;
    }
}
