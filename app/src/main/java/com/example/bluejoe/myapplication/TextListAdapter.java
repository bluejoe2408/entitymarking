package com.example.bluejoe.myapplication;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Yunzhe on 2017/11/26.
 */

public class TextListAdapter extends ArrayAdapter<TextList> {

    private int resourceId;

    public TextListAdapter(@NonNull Context context, int resource, @NonNull List<TextList> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextList textList = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.textName = (TextView) view.findViewById(R.id.text_name);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        assert textList != null;
        viewHolder.textName.setText(textList.getName());
        return view;
    }

    class ViewHolder {
        TextView textName;
    }
}
