package com.example.billsplitter.ui;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public abstract class ExtendedAdapter<T> extends BaseAdapter {
    private List<T> elements = new ArrayList<T>();
    private final LayoutInflater inflater;

    private final int layout;

    public ExtendedAdapter(Context context, int layout) {
        this.inflater = LayoutInflater.from(context);
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return elements.size();
    }

    @Override
    public T getItem(int position) {
        return elements.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final View view;
        if (convertView != null) {
            view = convertView;
        } else {
            view = inflater.inflate(layout, parent, false);
        }

        setFields(getItem(position), view);

        return view;
    }


    public void setElements(List<T> elements) {
        this.elements = elements;
    }
    public abstract void setFields(T element, View view);
}
