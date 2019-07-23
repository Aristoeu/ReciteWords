package com.example.recitewords.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;

public class BottomBar extends View{

    private Context context;

    public BottomBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    private int containerId;

    private List<Class> fragmentClassList = new ArrayList<>();
    private List<String> titleList = new ArrayList<>();
    private List<Integer> iconResBeforeList = new ArrayList<>();
    private List<Integer> iconResAfterList = new ArrayList<>();

    private List<Fragment> fragmentList = new ArrayList<>();

    private int itemCount;

    private Paint paint = new Paint();

    private List<Bitmap> iconBitmapBeforeList = new ArrayList<>();
    private List<Bitmap> iconBitmapAfterList = new ArrayList<>();
    private List<Rect> iconRectList = new ArrayList<>();

    private int currentCheckedIndex;
    private int firstCheckedIndex;

    private int titleColorBefore = Color.parseColor("#999999");
    private int titleColorAfter = Color.parseColor("#ff5d5e");

    public BottomBar setContainer(int containerId) {
        this.containerId = containerId;
        return this;
    }

    public BottomBar setTitleBeforeAndAfterColor(String beforeResCode, String AfterResCode) {//支持"#333333"这种形式
        titleColorBefore = Color.parseColor(beforeResCode);
        titleColorAfter = Color.parseColor(AfterResCode);
        return this;
    }


    public BottomBar addItem(Class fragmentClass, String title, int iconResBefore, int iconResAfter) {
        fragmentClassList.add(fragmentClass);
        titleList.add(title);
        iconResBeforeList.add(iconResBefore);
        iconResAfterList.add(iconResAfter);
        return this;
    }

    public void build() {
        itemCount = fragmentClassList.size();

        for (int i = 0; i < itemCount; i++) {
            Bitmap beforeBitmap = getBitmap(iconResBeforeList.get(i));
            iconBitmapBeforeList.add(beforeBitmap);

            Bitmap afterBitmap = getBitmap(iconResAfterList.get(i));
            iconBitmapAfterList.add(afterBitmap);

            Rect rect = new Rect();
            iconRectList.add(rect);

            Class clx = fragmentClassList.get(i);
            try {
                Fragment fragment = (Fragment) clx.newInstance();
                fragmentList.add(fragment);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        currentCheckedIndex = firstCheckedIndex;
        switchFragment(currentCheckedIndex);

        invalidate();
    }

    private Bitmap getBitmap(int resId) {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) context.getResources().getDrawable(resId);
        return bitmapDrawable.getBitmap();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        initParam();
    }

    private int titleBaseLine;
    private List<Integer> titleXList = new ArrayList<>();

    private int parentItemWidth;

    private void initParam() {
        if (itemCount != 0) {
            //单个item宽高
            parentItemWidth = getWidth() / itemCount;
            int parentItemHeight = getHeight();

            //图标边长
            int iconWidth1 = 25;
            int iconWidth = dp2px(iconWidth1);//先指定20dp
            int iconHeight1 = 25;
            int iconHeight = dp2px(iconHeight1);

            //图标文字margin
            int titleIconMargin = 1;
            int textIconMargin = dp2px(((float) titleIconMargin)/2);//先指定5dp，这里除以一半才是正常的margin，不知道为啥，可能是图片的原因

            int titleSizeInDp = 10;
            int titleSize = dp2px(titleSizeInDp);//这里先指定10dp
            paint.setTextSize(titleSize);
            Rect rect = new Rect();
            paint.getTextBounds(titleList.get(0), 0, titleList.get(0).length(), rect);
            int titleHeight = rect.height();

            int iconTop = (parentItemHeight - iconHeight - textIconMargin - titleHeight)/2;
            titleBaseLine = parentItemHeight - iconTop;

            int firstRectX = (parentItemWidth - iconWidth) / 2;//第一个icon的左
            for (int i = 0; i < itemCount; i++) {
                int rectX = i * parentItemWidth + firstRectX;

                Rect temp = iconRectList.get(i);

                temp.left = rectX;
                temp.top = iconTop ;
                temp.right = rectX + iconWidth;
                temp.bottom = iconTop + iconHeight;
            }

            for (int i = 0; i < itemCount; i ++) {
                String title = titleList.get(i);
                paint.getTextBounds(title, 0, title.length(), rect);
                titleXList.add((parentItemWidth - rect.width()) / 2 + parentItemWidth * i);
            }
        }
    }

    private int dp2px(float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (itemCount != 0) {
            //画背景
            paint.setAntiAlias(false);
            for (int i = 0; i < itemCount; i++) {
                Bitmap bitmap = null;
                if (i == currentCheckedIndex) {
                    bitmap = iconBitmapAfterList.get(i);
                } else {
                    bitmap = iconBitmapBeforeList.get(i);
                }
                Rect rect = iconRectList.get(i);
                canvas.drawBitmap(bitmap, null, rect, paint);//null代表bitmap全部画出
            }

            //画文字
            paint.setAntiAlias(true);
            for (int i = 0; i < itemCount; i ++) {
                String title = titleList.get(i);
                if (i == currentCheckedIndex) {
                    paint.setColor(titleColorAfter);
                } else {
                    paint.setColor(titleColorBefore);
                }
                int x = titleXList.get(i);
                canvas.drawText(title, x, titleBaseLine, paint);
            }
        }
    }



    int target = -1;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN :
                target = withinWhichArea((int)event.getX());
                break;
            case MotionEvent.ACTION_UP :
                if (event.getY() < 0) {
                    break;
                }
                if (target == withinWhichArea((int)event.getX())) {
                    switchFragment(target);
                    currentCheckedIndex = target;
                    invalidate();
                }
                target = -1;
                break;
        }
        return true;

    }

    private int withinWhichArea(int x) { return x/parentItemWidth; }//从0开始

    private Fragment currentFragment;

    protected void switchFragment(int whichFragment) {
        Fragment fragment = fragmentList.get(whichFragment);
        int frameLayoutId = containerId;

        if (fragment != null) {
            FragmentTransaction transaction = ((AppCompatActivity)context).getSupportFragmentManager().beginTransaction();
            if (fragment.isAdded()) {
                if (currentFragment != null) {
                    transaction.hide(currentFragment).show(fragment);
                } else {
                    transaction.show(fragment);
                }
            } else {
                if (currentFragment != null) {
                    transaction.hide(currentFragment).add(frameLayoutId, fragment);
                } else {
                    transaction.add(frameLayoutId, fragment);
                }
            }
            currentFragment = fragment;
            transaction.commit();
        }
    }
}
