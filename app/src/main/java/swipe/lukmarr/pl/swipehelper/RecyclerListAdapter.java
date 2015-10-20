/*
 * Copyright (C) 2015 Paul Burke
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package swipe.lukmarr.pl.swipehelper;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import swipe.lukmarr.pl.swipehelper.swipe.Item;
import swipe.lukmarr.pl.swipehelper.swipe.ItemTouchHelperAdapter;
import swipe.lukmarr.pl.swipehelper.swipe.ItemTouchHelperViewHolder;


/**
 * Simple RecyclerView.Adapter that implements {@link ItemTouchHelperAdapter} to respond to move and
 * dismiss events from a {@link android.support.v7.widget.helper.ItemTouchHelper}.
 *
 * @author Paul Burke (ipaulpro)
 */
public class RecyclerListAdapter extends RecyclerView.Adapter<RecyclerListAdapter.ItemViewHolder>
        implements ItemTouchHelperAdapter {
    public static final String TAG = RecyclerListAdapter.class.getSimpleName();
    private final List<Item> mItems = new ArrayList<>();
    private SwipeListener listener;
    private View[] views;

    public interface SwipeListener {
        Context getContext();
    }

    public RecyclerListAdapter(@NonNull List<Item> dataSet, SwipeListener listener) {
        mItems.addAll(dataSet);
        this.listener = listener;
        views = new View[mItems.size()];
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder ");
        Item item = mItems.get(position);
        String message;
        switch (item.getState()) {
            case Item.STATE.LEFT: {
                message = "swiped left";
                break;
            }
            case Item.STATE.RIGHT: {
                message = "swiped right";
                break;
            }
            default: {
                message = "not swiped";
                break;
            }
        }
        String text = item.getText() + " " + message;
        holder.offerTitle.setText(text);
        if (views[position] == null)
            views[position] = holder.itemView;
//        holder.itemView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                views[position] = holder.itemView;
//                return false;
//            }
//        });
    }

    int lastPosition = -1;

    private void setAnimation(View viewToAnimate, int position) {
        Log.d(TAG, "setAnimation (" + position + "," + lastPosition + ")");
//        if (position > lastPosition) {
        Animation animation = AnimationUtils.loadAnimation(listener.getContext(),
                android.R.anim.fade_in);
        viewToAnimate.startAnimation(animation);
//            lastPosition = position;
//        }
    }

    @Override
    public void onItemSwiped(int position, boolean isSwipedLeft) {
        Log.d(TAG, "onItemSwiped ");
        Item item = mItems.get(position);
        if (isSwipedLeft) {
            item.onSwipeLeft();
        } else {
            item.onSwipeRight();
        }
//        if (views[position] != null) {
//            setAnimation(views[position], position);
//            views[position] = null;
//        }

        notifyDataSetChanged();
    }


    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        Log.d(TAG, "onItemMove ");
        int state = mItems.get(fromPosition).getState();
        String prev = mItems.remove(fromPosition).getText();
        Item item = new Item(prev);
        item.setState(state);
        mItems.add(toPosition > fromPosition ? toPosition - 1 : toPosition, item);
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder implements
            ItemTouchHelperViewHolder {

        public View v;
        //        public RelativeLayout mContainer;
        public FrameLayout parent;
        public TextView offerTitle;//, offerDescription, offerCount, offerValidity;

        public ItemViewHolder(View v) {
            super(v);
            this.v = v;
            offerTitle = (TextView) v.findViewById(R.id.drag_handle);
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }
    }
}