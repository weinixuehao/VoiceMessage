package jigao.oggspeextest;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Log;

/**
 * Created by chenlong on 16/9/1.
 */
public class LockSwingDrawable extends DoorBaseDrawable {
    private static final String TAG = LockSwingDrawable.class.getSimpleName();
    private Paint paint = new Paint();
    private int degrees;
    private int alpha = 255;
    private float scale = 1f;
    private ValueAnimator openingAnim;
    private Context context;
    private BitmapManager bitmapWrapper;
    private Bitmap bitmap;
    private Animator[] recordAnimators = new Animator[3];

    public LockSwingDrawable(Context context) {
        this.context = context;
        bitmapWrapper = new BitmapManager(context.getResources());
        bitmap = bitmapWrapper.getBitmap(R.mipmap.door_lock_icon_1);
    }

    @Override
    public void setAvaiable(boolean avaiable) {
        super.setAvaiable(avaiable);
        bitmap = bitmapWrapper.getBitmap(isAvaiable() ? R.mipmap.door_lock_icon_1 : R.mipmap.door_lock_icon_4);
    }

    public void startOpenAnim() {
        setEnd(false);
        reset();
        bitmap = bitmapWrapper.getBitmap(isAvaiable() ? R.mipmap.door_lock_icon_1 : R.mipmap.door_lock_icon_4);
        openingAnim = ValueAnimator.ofInt(-20, 20);
        openingAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                degrees = (int) animation.getAnimatedValue();
                invalidateSelf();
            }
        });
        openingAnim.setRepeatCount(ObjectAnimator.INFINITE);
        openingAnim.setDuration(600);
        openingAnim.setRepeatMode(ObjectAnimator.REVERSE);
        openingAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                endAnim();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        openingAnim.start();
        recordAnimators[0] = openingAnim;
    }

    private void reset() {
        isOpened = false;
        for (Animator animator : recordAnimators) {
            if (animator == null || !animator.isRunning()) {
                continue;
            }

            animator.end();
        }
    }

    private boolean isOpened;
    public void startStopAnim(boolean isOpening) {
        if (openingAnim == null || !openingAnim.isRunning()) {
            return;
        }

        this.isOpened = isOpening;
        openingAnim.end();
    }

    private void endAnim() {
        degrees = 0;
        ValueAnimator alphaAnimator = ValueAnimator.ofInt(255, 0);
        alphaAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                alpha = (int) animation.getAnimatedValue();
            }
        });

        ValueAnimator scaleAnimator = ValueAnimator.ofFloat(1f, 0.2f);
        scaleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                scale = (float) animation.getAnimatedValue();
                Log.i(TAG, "scale:" + scale);
                invalidateSelf();
            }
        });

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(alphaAnimator).with(scaleAnimator);
        animatorSet.setDuration(300);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Log.i(TAG, "onAnimationEnd");
                if (isOpened) {
                    onOpenDoorListener.onOpenDoor();
                }
                bitmap = bitmapWrapper.getBitmap(isOpened ? R.mipmap.door_lock_icon_2 : R.mipmap.door_lock_icon_3);
                resultAnim();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorSet.start();
        recordAnimators[1] = animatorSet;
    }

    private void resultAnim() {
        ValueAnimator alphaAnimator = ValueAnimator.ofInt(0, 255);
        alphaAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                alpha = (int) animation.getAnimatedValue();
            }
        });

        ValueAnimator scaleAnimator = ValueAnimator.ofFloat(0.2f, 1f);
        scaleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                scale = (float) animation.getAnimatedValue();
                Log.i(TAG, "scale:" + scale);
                invalidateSelf();
            }
        });

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(alphaAnimator).with(scaleAnimator);
        animatorSet.setDuration(300);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Log.i(TAG, "onAnimationEnd");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bitmap = bitmapWrapper.getBitmap(R.mipmap.door_lock_icon_1);
                        invalidateSelf();
                        setEnd(true);
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
        animatorSet.start();
        recordAnimators[2] = animatorSet;
    }

    @Override
    public void draw(Canvas canvas) {
        int height = getIntrinsicHeight();
        int widht = getIntrinsicWidth();
        int px = canvas.getWidth() / 2;
        int py = canvas.getHeight() / 2;
        canvas.rotate(degrees, px, py);
        int left = px - (bitmap.getWidth() / 2);
        int top = py - (bitmap.getHeight() / 2);
        canvas.scale(scale, scale, px, py);
        Paint paint = new Paint();
        paint.setAlpha(alpha);
        canvas.drawBitmap(bitmap, left, top, paint);
    }

//    @Override
//    public int getIntrinsicHeight() {
//        return bitmap.getHeight();
//    }
//
//    @Override
//    public int getIntrinsicWidth() {
//        return bitmap.getWidth();
//    }


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


    static class BitmapManager {
        private int mRes;
        private Bitmap bitmap;
        private Resources resources;

        public BitmapManager(Resources resources) {
            this.resources = resources;
        }

        private Bitmap getBitmap(int res) {
            if (bitmap == null) {
                bitmap = BitmapFactory.decodeResource(resources, res);
                this.mRes = res;
                return bitmap;
            }

            if (this.mRes == res) {
                return bitmap;
            }

            bitmap.recycle();
            bitmap = BitmapFactory.decodeResource(resources, res);
            this.mRes = res;
            return bitmap;
        }
    }

    private OnOpenDoorListener onOpenDoorListener;

    public void setOnOpenDoorListener(OnOpenDoorListener onOpenDoorListener) {
        this.onOpenDoorListener = onOpenDoorListener;
    }
    public interface OnOpenDoorListener {
        public void onOpenDoor();
    }
}
