package com.seasonsread.app.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.seasonsread.app.R;

/**
 * Created by ZhanTao on 1/17/15.
 */
public class LoadMoreFooter {


    public static enum State{
        IDLE, LOADING, END
    }

    private State mState = State.IDLE;
    private View mFooter;
    private TextView tvTip;
    private ProgressBar mProgressBar;

    public LoadMoreFooter(Context context){
        mFooter = LayoutInflater.from(context).inflate(R.layout.loading_footer, null);
        tvTip = (TextView)mFooter.findViewById(R.id.tv_tip);
        mProgressBar = (ProgressBar)mFooter.findViewById(R.id.progressBar);
        setState(State.IDLE);
    }

    public State getState(){
        return mState;
    }

    public void setState(State state){
        if(mState == state)
            return;
        mState = state;
        mFooter.setVisibility(View.VISIBLE);
        switch (state){
            case IDLE:
                mFooter.setVisibility(View.GONE);
                break;
            case END:
                tvTip.setText("The End");
                mProgressBar.setVisibility(View.GONE);
                break;
            case LOADING:
                tvTip.setText("Loading...");
                mProgressBar.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    public void setState(final State state, long delay){
        mFooter.postDelayed(new Runnable() {
            @Override
            public void run() {
                setState(state);
            }
        }, delay);
    }

    public View getView(){
        return mFooter;
    }

}
