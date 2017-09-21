package wu.victor.snr.frameworks.database.store;

import com.raizlabs.android.dbflow.structure.ModelAdapter;

import java.util.List;

import wu.victor.snr.business.models.NewsModel;

/**
 * Created by wuguonan on 2017/3/16 0016.
 */

public final class DatabaseUtils {

  public static void saveToDb(List<NewsModel> models) {
    if (models != null && models.size() > 0) {
      ModelAdapter adapter = models.get(0).getModelAdapter();
      if (adapter != null) {
        adapter.saveAll(models);
      }
    }
  }

}
