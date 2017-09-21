package wu.victor.snr.business.viewmodels;

import android.databinding.ObservableField;
import android.util.Log;

import com.foxframe.segment.core.Segment;
import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;

import rx.functions.Action0;
import wu.victor.snr.business.segment.NewsDetailSegment;
import wu.victor.snr.business.models.NewsModel;

/**
 * Created by wuguonan on 2017/3/3 0003.
 */

public class NewsItemViewModel implements ViewModel {

  public NewsItemViewModel(NewsModel newsItem, Segment segment) {
    this.newsItem = newsItem;
    title.set(newsItem.getTitle());
    url.set(newsItem.getThumbnail_pic_s());
    author.set(newsItem.getAuthor_name());
    this.segment = segment;
  }

  private NewsModel newsItem;

  private Segment segment;

  public final ObservableField<String> title = new ObservableField<>();

  public final ObservableField<String> url = new ObservableField<>();

  public final ObservableField<String> author = new ObservableField<>();

  public ReplyCommand onClickCommand = new ReplyCommand(new Action0() {
    @Override
    public void call() {
      if (newsItem != null) {
        Log.e("wgn", newsItem.getUrl());
        NewsDetailSegment target = new NewsDetailSegment(newsItem);
        NewsDetailSegment.attachToCurrentStack(segment, target);
      }
    }
  });

}
