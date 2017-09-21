package wu.victor.snr.business.segment;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.foxframe.segment.extention.SegStackSegment;
import com.foxframe.ui.FoxViewStack;

import wu.victor.snr.R;
import wu.victor.snr.business.constants.NewsTypes;

/**
 * Created by wuguonan on 2016/9/2 0002.
 */
public class MyStackWinSegment extends SegStackSegment implements MyWin {

  LinearLayout myView;
  FoxViewStack slot;
  TextView tab;

  int count = 0;
  boolean run = false;
  NewsTypes.Types type;

  public MyStackWinSegment(NewsTypes.Types type) {
    this.type = type;
  }

  @Override
  protected void onAttach(Activity activity) {
    super.onAttach(activity);
    pushSegment(new MyNewsListSegment(type));
  }

  @Override
  protected View onCreateView(Activity activity) {

    myView = (LinearLayout) View.inflate(getAppContext(), R.layout.fox_stack_win_seg, null);
    slot = (FoxViewStack) myView.findViewById(R.id.content_slot);
    slot.setUnderLayerDoAnim(true);
    setChildViewSlot(slot);
//    int color = SegmentUtils.getRandomColor();
//    float grade = type / 12;
//    int color = getColor();
//    myView.setBackgroundColor(color);
    tab = new TextView(getAppContext());
    tab.setBackgroundColor(0xff123456);
    tab.setTextColor(Color.GRAY);
    tab.setTextSize(15);
    tab.setGravity(Gravity.CENTER);
    tab.setText(type.getName());

    return myView;
  }

  @Override
  protected void onResume() {
    super.onResume();
    startRunning();
    tab.setTextColor(Color.WHITE);
    tab.setText(type.getName());
  }

  @Override
  protected void onPause() {
    super.onPause();
    stopRunning();
    tab.setTextColor(Color.GRAY);
    tab.setText(type.getName());
  }

  @Override
  public View getTab() {
    return tab;
  }

  private void running() {
    if (run) {
      count++;
      if (count == 10) {
        count = 0;
      }
      myView.postDelayed(new Runnable() {
        @Override
        public void run() {
          running();
        }
      }, 200);
    }
  }

  private void stopRunning() {
    run = false;
  }

  private void startRunning() {
    run = true;
//    running();
  }

  @Override
  protected void moveViewToForground(View aView) {
    ViewParent vp = aView.getParent();
    if (vp != null) {
      if (vp.equals(slot)) {
        return;
      }
      if (vp instanceof ViewGroup) {
        ((ViewGroup) vp).removeView(aView);
      }
    }
    slot.pushView(aView);
  }

  @Override
  public boolean onKeyDown(int aKeyCode, KeyEvent aEvent) {
    if (getChildrenCount() > 1) {
      return true;
    }
    return super.onKeyDown(aKeyCode, aEvent);
  }

  @Override
  public boolean onKeyUp(int aKeyCode, KeyEvent aEvent) {

    if (getChildrenCount() > 1) {
      popSegment();
      return true;
    }
    return super.onKeyUp(aKeyCode, aEvent);
  }

  private int getColor(float grade) {
    int red = (int) (0 * 255);
    int green = (int) (60 + grade * 42);
    int blue = (int) (160 + grade * 95);
    return 0xff000000 + (red << 16) + (green << 8) + (blue);
  }
}
