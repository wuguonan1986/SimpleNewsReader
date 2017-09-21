package wu.victor.snr.business.segment;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.foxframe.segment.core.Segment;
import com.foxframe.segment.extention.SegStackSegment;

import wu.victor.snr.R;
import wu.victor.snr.databinding.NewsDetailPageBinding;
import wu.victor.snr.business.viewmodels.NewsDetailViewModel;
import wu.victor.snr.business.models.NewsModel;

/**
 * Created by wuguonan on 2017/3/14 0014.
 */

public class NewsDetailSegment extends Segment {

  NewsDetailViewModel viewModel;

  public NewsDetailSegment(NewsModel model) {
    super();
    viewModel = new NewsDetailViewModel(model);
  }

  @Override
  protected void onResume() {
    super.onResume();
    viewModel.loadData();
    viewModel.saveHistory();
  }

  @Override
  protected void onChildSegmentViewCreated(View aView, Segment aChildSegment) {

  }

  @Override
  protected void onRemoveChildSegmentView(View aView, Segment aChildSegment) {

  }

  @Override
  protected View onCreateView(Activity activity) {
    NewsDetailPageBinding binding = DataBindingUtil.inflate(LayoutInflater.from(activity),
        R.layout.news_detail_page, null, false);
    binding.setDetailViewModel(viewModel);
    View view = binding.getRoot();
    WebView webView = (WebView) view.findViewById(R.id.webview);
    if (webView != null) {
      webView.getSettings().setJavaScriptEnabled(true);
      webView.setWebViewClient(new WebViewClient());
    }

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
