package wu.victor.snr.business.segment;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;

import com.foxframe.segment.core.Segment;
import com.foxframe.segment.extention.SegStackSegment;

import wu.victor.snr.R;
import wu.victor.snr.business.viewmodels.NewsHistoryListViewModel;
import wu.victor.snr.databinding.HistoriesNewsListViewBinding;

/**
 * Created by wuguonan on 2017/3/16 0016.
 */

public class MyHistorySegment extends Segment  {

  NewsHistoryListViewModel newsListViewModel;

  public MyHistorySegment() {
  }

  @Override
  protected void onResume() {
    super.onResume();
    if (newsListViewModel == null) {
      newsListViewModel = new NewsHistoryListViewModel(this);
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
    HistoriesNewsListViewBinding binding = DataBindingUtil.inflate(LayoutInflater.from(activity),
        R.layout.histories_news_list_view, null, false);
    if (newsListViewModel == null) {
      newsListViewModel = new NewsHistoryListViewModel(this);
    }
    binding.setHistoryListViewModel(newsListViewModel);
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
