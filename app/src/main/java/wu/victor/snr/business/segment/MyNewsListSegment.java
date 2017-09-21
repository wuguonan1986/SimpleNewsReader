package wu.victor.snr.business.segment;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;

import com.foxframe.segment.core.Segment;

import wu.victor.snr.R;
import wu.victor.snr.databinding.NewsListViewBinding;
import wu.victor.snr.business.constants.NewsTypes;
import wu.victor.snr.business.viewmodels.NewsListViewModel;

/**
 * Created by wuguonan on 2017/3/14 0014
 */

public class MyNewsListSegment extends Segment {

  NewsListViewModel newsListViewModel;

  NewsTypes.Types type;

  public MyNewsListSegment(NewsTypes.Types type) {
    this.type = type;
  }

  @Override
  protected void onResume() {
    super.onResume();
    if (newsListViewModel == null) {
      newsListViewModel = new NewsListViewModel(type, this);
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
    NewsListViewBinding binding = DataBindingUtil.inflate(LayoutInflater.from(activity),
        R.layout.news_list_view, null, false);
    if (newsListViewModel == null) {
      newsListViewModel = new NewsListViewModel(type, this);
    }
    binding.setItemListViewModel(newsListViewModel);
    return binding.getRoot();
  }

  @Override
  protected void onDestroyView() {

  }
}
