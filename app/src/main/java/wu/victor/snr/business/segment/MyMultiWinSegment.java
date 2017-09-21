package wu.victor.snr.business.segment;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.foxframe.segment.core.Segment;
import com.foxframe.segment.extention.MultiTabSegment;

import wu.victor.snr.R;
import wu.victor.snr.business.constants.NewsTypes;

/**
 * Created by wuguonan on 2016/9/1 0001.
 */
public class MyMultiWinSegment extends MultiTabSegment {


  ViewPager slot;
  LinearLayout winList;

  boolean shouldExit = false;

  @Override
  protected void onAttach(Activity activity) {
    super.onAttach(activity);
    addNewTab(new MyStackWinSegment(NewsTypes.Types.TOP), true, 0);
    addNewTab(new MyStackWinSegment(NewsTypes.Types.SHEHUI), false, 1);
    addNewTab(new MyStackWinSegment(NewsTypes.Types.YULE), false, 2);
    addNewTab(new MyStackWinSegment(NewsTypes.Types.TIYU), false, 3);
    addNewTab(new MyStackWinSegment(NewsTypes.Types.KEJI), false, 4);
    addNewTab(new MyStackWinSegment(NewsTypes.Types.CAIJING), false, 5);
    addNewTab(new MyStackWinSegment(NewsTypes.Types.JUNSHI), false, 6);
    addNewTab(new MyStackWinSegment(NewsTypes.Types.GUOJI), false, 7);
    addNewTab(new MyStackWinSegment(NewsTypes.Types.GUONEI), false, 8);
    addNewTab(new MyStackWinSegment(NewsTypes.Types.SHISHANG), false, 9);

  }


  @Override
  protected void onChildSegmentViewCreated(View aView, final Segment aChildSegment) {
    super.onChildSegmentViewCreated(aView, aChildSegment);
    if (winList != null && aChildSegment instanceof MyWin) {
      View view = ((MyWin) aChildSegment).getTab();
      LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
          LinearLayout.LayoutParams.MATCH_PARENT);
      lp.weight = 1;
      lp.gravity = Gravity.CENTER_VERTICAL;
      int index = getChildIndex(aChildSegment);
      if (index < winList.getChildCount()) {
        winList.addView(view, index, lp);
      } else {
        winList.addView(view, lp);
      }

//      winList.addView(view, lp);
      view.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          setFocusToChild(aChildSegment);
        }
      });
    }
    pagerAdapter.notifyDataSetChanged();
  }

  @Override
  protected void onRemoveChildSegmentView(View aView, Segment aChildSegment) {
    super.onRemoveChildSegmentView(aView, aChildSegment);
    if (winList != null && aChildSegment instanceof MyWin) {
      winList.removeView(((MyWin) aChildSegment).getTab());
    }
    pagerAdapter.notifyDataSetChanged();
  }

  @Override
  protected View onCreateView(Activity activity) {
    LinearLayout multiWin = (LinearLayout) View.inflate(getAppContext(),
        R.layout.fox_multi_win_seg, null);
    setMyView(multiWin);
    slot = (ViewPager) multiWin.findViewById(R.id.mutli_win_slot);
    winList = (LinearLayout) multiWin.findViewById(R.id.win_list);

    slot.setAdapter(pagerAdapter);
    slot.addOnPageChangeListener(listener);

    return multiWin;
  }

  @Override
  protected void onChildResumed(Segment aChildSegment) {
    super.onChildResumed(aChildSegment);
    slot.setCurrentItem(getChildIndex(aChildSegment));
  }

  @Override
  protected void onDestroyView() {
    super.onDestroyView();
  }

  @Override
  protected void moveViewToForground(View aView) {

  }


  PagerAdapter pagerAdapter = new PagerAdapter() {
    @Override
    public int getCount() {
      return getChildrenCount();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
      return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position,
                            Object object) {
      // TODO Auto-generated method stub
      if (object != null && object instanceof View) {
        Segment segment = getChildAt(position);
        if (segment != null && object.equals(segment.getView())) {
          return;
        }
        container.removeView((View) object);
      }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
      View view = getChildAt(position).getView();
      if (view != null) {
        ViewParent vp = view.getParent();
        if (vp != null && vp instanceof ViewGroup) {
          ((ViewGroup) vp).removeView(view);
        }
        if (position < container.getChildCount()) {
          container.addView(view, position);
        } else {
          container.addView(view);
        }
//        container.addView(view, position);
//        container.addView(view);
      }
      return view;
    }

    @Override
    public int getItemPosition(Object object) {
      if (object == null) {
        return POSITION_NONE;
      }
      int cnt = getChildrenCount();
      for (int i = 0; i < cnt; i++) {
        if (object.equals(getChildAt(i).getView())) {
          return i;
        }
      }
      return POSITION_NONE;
    }


  };

  ViewPager.OnPageChangeListener listener = new ViewPager.OnPageChangeListener() {
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
      setFocusToChild(getChildAt(position));
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
  };


  @Override
  public boolean onKeyDown(int aKeyCode, KeyEvent aEvent) {
    return true;
//    return super.onKeyDown(aKeyCode, aEvent);

  }

  private Runnable runnable = new Runnable() {
    @Override
    public void run() {
      shouldExit = false;
    }
  };

  private Handler handler = new Handler(Looper.getMainLooper());

  @Override
  public boolean onKeyUp(int aKeyCode, KeyEvent aEvent) {

    if (shouldExit) {
      getActivity().finish();
      handler.removeCallbacks(runnable);
    } else {

      Toast toast = Toast.makeText(getAppContext(), "再按一次后退退出程序", Toast.LENGTH_SHORT);
//      toast.setText("再按一次后退退出程序");
      toast.show();
      shouldExit = true;
      handler.postDelayed(runnable, 1500);
    }

    return true;


//    return super.onKeyUp(aKeyCode, aEvent);
  }
}
