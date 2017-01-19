package jigao.oggspeextest;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.animation.DecelerateInterpolator;

/**
 * Created by chenlong on 16/8/31.
 */
public class BackgroudDoorDrawable extends DoorBaseDrawable {
    private static final String TAG = BackgroudDoorDrawable.class.getSimpleName();
    private Paint paint;
    private int value = 100;
    private final int size;
    private int doorWidth;
    private int strokewidth;

    public BackgroudDoorDrawable(Context context) {
        paint = new Paint();
        Resources res = context.getResources();
        size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 70, res.getDisplayMetrics());
        strokewidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, res.getDisplayMetrics());
        doorWidth = size / 2;
        value = doorWidth;
    }

    public void openDoorAnim() {
        setEnd(false);
        DecelerateInterpolator decelerateInterpolator = new DecelerateInterpolator(1f);
        ValueAnimator valueAnimator = ValueAnimator.ofInt(doorWidth, doorWidth / 5);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                value = (int) animation.getAnimatedValue();
                invalidateSelf();
                Log.i(TAG, "value:" + value);
            }
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        closeDoorAnim();
                    }
                }, 3000);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        valueAnimator.setInterpolator(decelerateInterpolator);
        valueAnimator.setDuration(300);
        valueAnimator.start();
    }

    private void closeDoorAnim() {
        DecelerateInterpolator decelerateInterpolator = new DecelerateInterpolator(1f);
        ValueAnimator valueAnimator = ValueAnimator.ofInt(doorWidth / 5, doorWidth);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                value = (int) animation.getAnimatedValue();
                invalidateSelf();
            }
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                setEnd(true);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        valueAnimator.setInterpolator(decelerateInterpolator);
        valueAnimator.setDuration(300);
        valueAnimator.start();
    }

    @Override
    public void draw(final Canvas canvas) {
        int width = canvas.getWidth();
        int height = canvas.getHeight();
//        rotateDrawable.draw(canvas);

        int doorColor = Color.parseColor(isAvaiable() ? "#CCEAFB" : "#F1F1F1");
        Rect rect = new Rect(strokewidth, strokewidth, value - (strokewidth / 2), size - strokewidth);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(doorColor);
        canvas.drawRect(rect, paint);

        // border
        paint.setStrokeWidth(strokewidth);
        paint.setStyle(Paint.Style.STROKE);
        int strokeColor = Color.parseColor(isAvaiable() ? "#80C9F5" : "#DCDCDC");
        paint.setColor(strokeColor);
        canvas.drawRect(rect, paint);

        rect = new Rect(size - value + (strokewidth / 2), strokewidth, size - strokewidth, size - strokewidth);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(doorColor);
        canvas.drawRect(rect, paint);

        // border
        paint.setStrokeWidth(strokewidth);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(strokeColor);
        canvas.drawRect(rect, paint);

        Log.i(TAG, "width:" + width + " height:" + height);
    }

    @Override
    public int getIntrinsicHeight() {
        return size;
    }

    @Override
    public int getIntrinsicWidth() {
        return size;
    }

    @Override
    public void setAlpha(int alpha) {
        paint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        paint.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.OPAQUE;
    }

}
