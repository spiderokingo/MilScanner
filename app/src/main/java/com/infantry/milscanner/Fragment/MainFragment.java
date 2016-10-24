package com.infantry.milscanner.Fragment;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.infantry.milscanner.Activity.MainActivity;
import com.infantry.milscanner.Activity.ScanActivity;
import com.infantry.milscanner.Activity.TrackingActivity;
import com.infantry.milscanner.R;
import com.infantry.milscanner.Utils.Singleton;
import com.infantry.milscanner.ViewHolder.MainFragmentAdapter;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {


    @Bind(R.id.listView)
    ListView listView;

    public MainFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);

        String[] values = new String[] { "อาวุธ", "เครื่องมือสื่อสาร",
                "เครื่องสนาม", "GPS Tracking" };

        final ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < values.length; ++i) {
            list.add(values[i]);
        }
        final MainFragmentAdapter adapter = new MainFragmentAdapter(getContext(),
                android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);

                switch (position){
                    case 0:
                        startActivity(new Intent(getContext(), ScanActivity.class));
                        break;
                    case 1:
                        Singleton.toast(getContext(), "เครื่องมือสื่อสาร", Toast.LENGTH_SHORT);
                        break;
                    case 2:
                        Singleton.toast(getContext(), "เครื่องสนาม", Toast.LENGTH_SHORT);
                        break;
                    case 3:
                        startActivity(new Intent(getContext(), TrackingActivity.class));
                        break;
                }
            }

        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
