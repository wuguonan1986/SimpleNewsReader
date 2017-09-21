package wu.victor.snr.business.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;

/**
 * Created by wuguonan on 2017/3/18 0018.
 */

public class RecyclerViewItemDec extends DividerItemDecoration {
  /**
   * Creates a divider {@link RecyclerView.ItemDecoration} that can be used with a
   * {@link LinearLayoutManager}.
   *
   * @param context     Current context, it will be used to access resources.
   * @param orientation Divider orientation. Should be {@link #HORIZONTAL} or {@link #VERTICAL}.
   */
  public RecyclerViewItemDec(Context context, int orientation) {
    super(context, orientation);
  }

  @Override
  public void setDrawable(@NonNull Drawable drawable) {
    super.setDrawable(drawable);
  }
}
