package jigao.oggspeextest;

import android.graphics.drawable.Drawable;

/**
 * Created by chenlong on 16/9/2.
 */
public abstract class DoorBaseDrawable extends Drawable{
    private boolean isEnd = true;
    private boolean isAvaiable = true;

    public boolean isAvaiable() {
        return isAvaiable;
    }

    public void setAvaiable(boolean avaiable) {
        isAvaiable = avaiable;
        invalidateSelf();
    }

    public boolean isEnd() {
        return isEnd;
    }

    public void setEnd(boolean isEnd) {
        this.isEnd = isEnd;
    }
}
