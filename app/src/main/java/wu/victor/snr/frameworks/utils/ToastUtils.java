package wu.victor.snr.frameworks.utils;

import android.widget.Toast;

import wu.victor.snr.app.SnrApplication;

/**
 * Created by wuguonan on 2017/3/16 0016.
 */

public final class ToastUtils {

  public static void showToast(String message) {
    Toast.makeText(SnrApplication.getInstance(), message, Toast.LENGTH_SHORT).show();
  }
}
