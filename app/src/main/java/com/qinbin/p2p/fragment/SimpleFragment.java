package com.qinbin.p2p.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * Created by teacher on 2016/4/20.
 */
public class SimpleFragment extends Fragment {
    String title;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.v(title, "4 onActivityCreated");
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        Log.v(title, "1 onAttach");
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.v(title, "2 onCreate " + getTitleFromBundle(savedInstanceState));
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        Log.v(title, "二 onDestroy");
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        Log.v(title, "三 onDestroyView");
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        Log.v(title, "一 onDetach");
        super.onDetach();
    }

    @Override
    public void onPause() {
        Log.v(title, "六 onPause");
        super.onPause();
    }

    @Override
    public void onStart() {
        Log.v(title, "5 onStart");
        super.onStart();
    }

    @Override
    public void onStop() {
        Log.v(title, "五 onStop");
        super.onStop();
    }

    @Override
    public void onResume() {
        Log.v(title, "6 onResume");
        super.onResume();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.v(title, "3 onCreateView " + getTitleFromBundle(savedInstanceState));
        TextView tv = new TextView(container.getContext());
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(80);

        tv.setText(title);

        return tv;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.w(title, "onSaveInstanceState");
        outState.putString("title", title);
        super.onSaveInstanceState(outState);
    }

    public static SimpleFragment create(String title) {
        SimpleFragment result = new SimpleFragment();
        Bundle bundle = new Bundle();
        bundle.putString("Title", title);
        result.setArguments(bundle);
        return result;
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        title = getArguments().getString("Title");
    }
    private String getTitleFromBundle(Bundle bundle){
        if(bundle==null){
            return null;
        }

        return bundle.getString("title");
    }
}
