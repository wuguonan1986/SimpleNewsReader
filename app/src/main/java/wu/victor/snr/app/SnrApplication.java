package wu.victor.snr.app;

import android.app.Application;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

import cn.sharesdk.framework.ShareSDK;

/**
 * Created by wuguonan on 2017/3/15 0015.
 */

public class SnrApplication extends Application {

  private static SnrApplication instance;

  @Override
  public void onCreate() {
    super.onCreate();
    //初始化数据库
    FlowConfig flowConfig = new FlowConfig.Builder(this).build();
    FlowManager.init(flowConfig);
    instance = this;
    ShareSDK.initSDK(this);
  }

  public static SnrApplication getInstance() {
    return instance;
  }

}
