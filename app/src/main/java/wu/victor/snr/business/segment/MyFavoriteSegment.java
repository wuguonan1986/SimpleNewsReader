package wu.victor.snr.business.segment;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;

import com.foxframe.segment.core.Segment;
import com.foxframe.segment.extention.SegStackSegment;

import wu.victor.snr.R;
import wu.victor.snr.business.viewmodels.NewsFavoritesListViewModel;
import wu.victor.snr.databinding.FavoriteNewsListViewBinding;

/**
 * Created by wuguonan on 2017/3/15 0015.
 */

public class MyFavoriteSegment extends Segment {

  NewsFavoritesListViewModel newsListViewModel;

  public MyFavoriteSegment() {
  }

  @Override
  protected void onResume() {
    super.onResume();
    if (newsListViewModel == null) {
      newsListViewModel = new NewsFavoritesListViewModel(this);
    }
    newsListViewModel.loadDefaultData();
  }

  @Override
  protected void onChildSegmentViewCreated(View aView, Segment aChildSegment) {

  }

  @Override
  protected void onRemoveChildSegmentView(View aView, Segment aChildSegment) {

  }

  @Override
  protected View onCreateView(Activity activity) {
    FavoriteNewsListViewBinding binding = DataBindingUtil.inflate(LayoutInflater.from(activity),
        R.layout.favorite_news_list_view, null, false);
    if (newsListViewModel == null) {
      newsListViewModel = new NewsFavoritesListViewModel(this);
    }
    binding.setFavoriteListViewModel(newsListViewModel);
    return binding.getRoot();
  }

  @Override
  protected void onDestroyView() {

  }

  public static void attachToCurrentStack(Segment from, Segment target) {
    Segment parent = from.getParent();
    while (parent != null) {
      if (parent instanceof SegStackSegment) {
        ((SegStackSegment) parent).pushSegment(target);
        return;
      }
      parent = parent.getParent();
    }
  }
}
