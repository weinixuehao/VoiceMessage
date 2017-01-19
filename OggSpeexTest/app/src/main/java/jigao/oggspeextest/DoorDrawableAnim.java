package jigao.oggspeextest;

import android.content.Context;
import android.graphics.drawable.LayerDrawable;

/**
 * Created by chenlong on 16/9/3.
 */
public class DoorDrawableAnim implements LockSwingDrawable.OnOpenDoorListener {
    private LayerDrawable layerDrawable;

    public DoorDrawableAnim(Context context) {
        DoorBaseDrawable[] layers = new DoorBaseDrawable[2];
        layers[0] = new BackgroudDoorDrawable(context);
        LockSwingDrawable lockSwingDrawable = new LockSwingDrawable(context);
        lockSwingDrawable.setOnOpenDoorListener(this);
        layers[1] = lockSwingDrawable;
        layerDrawable = new LayerDrawable(layers);
    }

    public LayerDrawable getLayerDrawable() {
        return layerDrawable;
    }

    @Override
    public void onOpenDoor() {
        BackgroudDoorDrawable backgroudDoorDrawable = (BackgroudDoorDrawable)
                layerDrawable.getDrawable(0);
        backgroudDoorDrawable.openDoorAnim();
    }

    public void startOpening() {
        if (!isEnd() || !isAvailable()) {
            return;
        }
        LockSwingDrawable lockSwingDrawable = (LockSwingDrawable) layerDrawable.getDrawable(1);
        lockSwingDrawable.startOpenAnim();
    }

    public void stopOpening(boolean isOpened) {
        if (!isAvailable() || isEnd()) {
            return;
        }
        LockSwingDrawable lockSwingDrawable = (LockSwingDrawable) layerDrawable.getDrawable(1);
        lockSwingDrawable.startStopAnim(isOpened);
    }

    public boolean isEnd() {
        return ((DoorBaseDrawable) layerDrawable.getDrawable(0)).isEnd()
                && ((DoorBaseDrawable) layerDrawable.getDrawable(1)).isEnd();
    }

    public boolean isAvailable() {
//        return layers[0].isAvaiable() && layers[1].isAvaiable();
        return ((DoorBaseDrawable) layerDrawable.getDrawable(0)).isAvaiable()
                && ((DoorBaseDrawable) layerDrawable.getDrawable(1)).isAvaiable();
    }

    public void setAvailable(boolean flag) {
        int num = layerDrawable.getNumberOfLayers();
        for (int i = 0; i < num; i++) {
            DoorBaseDrawable doorBaseDrawable = (DoorBaseDrawable) layerDrawable.getDrawable(i);
            doorBaseDrawable.setAvaiable(flag);
        }
    }
}
