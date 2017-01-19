package jigao.oggspeextest;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by chenlong on 16/9/1.
 */
public class DoorImageView extends ImageView implements LockSwingDrawable.OnOpenDoorListener{
    private static final String TAG = LockSwingDrawable.class.getSimpleName();
    private DoorBaseDrawable[] layers;

    public DoorImageView(Context context) {
        this(context, null);
    }

    public DoorImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DoorImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        layers = new DoorBaseDrawable[2];
        layers[0] =  new BackgroudDoorDrawable(context);
        LockSwingDrawable lockSwingDrawable = new LockSwingDrawable(context);
        lockSwingDrawable.setOnOpenDoorListener(this);
        layers[1] = lockSwingDrawable;
        LayerDrawable layerDrawable = new LayerDrawable(layers);
        setImageDrawable(layerDrawable);
    }

    public void startOpening() {
        if (!isEnd() || !isAvailable()) {
            return;
        }
        LockSwingDrawable lockSwingDrawable = (LockSwingDrawable) layers[1];
        lockSwingDrawable.startOpenAnim();
    }

    public void stopOpening(boolean isOpened) {
        if (!isAvailable() || isEnd()) {
            return;
        }
        LockSwingDrawable lockSwingDrawable = (LockSwingDrawable) layers[1];
        lockSwingDrawable.startStopAnim(isOpened);
    }

    @Override
    public void onOpenDoor() {
        BackgroudDoorDrawable backgroudDoorDrawable = (BackgroudDoorDrawable) layers[0];
        backgroudDoorDrawable.openDoorAnim();
    }

    public boolean isEnd() {
        return layers[0].isEnd() && layers[1].isEnd();
    }

    public boolean isAvailable() {
        return layers[0].isAvaiable() && layers[1].isAvaiable();
    }

    public void setAvailable(boolean flag) {
        for (DoorBaseDrawable doorBaseDrawable : layers) {
            doorBaseDrawable.setAvaiable(flag);
        }
    }
}
