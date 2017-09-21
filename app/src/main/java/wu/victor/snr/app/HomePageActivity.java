package wu.victor.snr.app;

import android.os.Bundle;

import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.foxframe.framework.SegmentActivity;

import wu.victor.snr.business.segment.MyMultiWinSegment;

/**
 * Created by wuguonan on 2017/3/1 0001
 */

public class HomePageActivity extends SegmentActivity {

//  NewsListViewModel newsListViewModel = new NewsListViewModel();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ImagePipelineFactory.initialize(getApplicationContext());
//    NewsListViewBinding binding = DataBindingUtil.setContentView(this, R.layout.news_list_view);
//    binding.setItemListViewModel(newsListViewModel);
//    newsListViewModel.loadData();
    MyMultiWinSegment mul = new MyMultiWinSegment();
    addSegment(mul);
  }


}
