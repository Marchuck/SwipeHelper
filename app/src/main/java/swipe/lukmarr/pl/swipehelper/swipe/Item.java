package swipe.lukmarr.pl.swipehelper.swipe;

/**
 * Created by lukasz on 20.10.15.
 */
public class Item {
    private String text;
    private int state;
    public static final String TAG = Item.class.getSimpleName();

    public void setText(String text) {
        this.text = text;
    }

    public void setState(int state) {

        this.state = state;
    }

    public interface STATE {
        int IDLE = 0;
        int LEFT = -1;
        int RIGHT = 1;
    }

    public Item(String text) {
        this.text = text;
        state = STATE.IDLE;
    }

    public void onSwipeLeft() {
        if (state == STATE.IDLE)
            state = STATE.LEFT;
        else if (state == STATE.RIGHT)
            state = STATE.IDLE;
    }

    public void onSwipeRight() {
        if (state == STATE.IDLE)
            state = STATE.RIGHT;
        else if (state == STATE.LEFT)
            state = STATE.IDLE;
    }

    public int getState() {
        return state;
    }

    public String getText() {
        return text;
    }
}
