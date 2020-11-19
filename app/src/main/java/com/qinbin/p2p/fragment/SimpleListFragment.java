package com.qinbin.p2p.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.UUID;

import com.qinbin.p2p.R;

/**
 * Created by teacher on 2016/4/27.
 */
public class SimpleListFragment extends Fragment {


    private View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_simple_list, container, false);
            ListView lv = (ListView) rootView.findViewById(R.id.simple_lv);
            // 给lv随机设置一些数据
            String randomString = UUID.randomUUID().toString();
            lv.setAdapter(new ArrayAdapter<String>(rootView.getContext(), android.R.layout.simple_list_item_1, randomString.split("\\B")));
        }
        return rootView;
    }
}
