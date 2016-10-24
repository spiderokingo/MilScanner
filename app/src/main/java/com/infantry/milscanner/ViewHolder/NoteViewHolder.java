package com.infantry.milscanner.ViewHolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.infantry.milscanner.Models.StringModel;
import com.infantry.milscanner.R;

import java.util.List;

/**
 * Created by visit on 8/24/15 AD.
 */


public class NoteViewHolder extends ArrayAdapter<StringModel> {

    public NoteViewHolder(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public NoteViewHolder(Context context, int resource, List<StringModel> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.viewholder_tracking, null);
        }

        StringModel item = getItem(position);

        if (item != null) {
            TextView tvStatus = (TextView) v.findViewById(R.id.tvTrackingStatus);

            if (tvStatus != null) {
                tvStatus.setText(item.Status);
                if(item.Mode == 1) {
                    //Success
                    tvStatus.setTextColor(getContext().getResources().getColor(R.color.md_green_300));
                }else{
                    //Failed
                    tvStatus.setTextColor(getContext().getResources().getColor(R.color.md_red_300));

                }
            }
        }

        return v;
    }
}
