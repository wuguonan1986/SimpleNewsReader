package wu.victor.snr.frameworks.database.store;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by wuguonan on 2017/3/15 0015.
 */
@Database(name = SnrDataBase.NAME, version = SnrDataBase.VERSION)
public class SnrDataBase {
  //数据库名称
  public static final String NAME = "SnrDataBase";

  //数据库版本号
  public static final int VERSION = 1;
}
