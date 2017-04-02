package com.example.nimolee.chatclient;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.Vector;

public class ListAdapterForMassage extends BaseAdapter {

    private Context _context;
    private Vector<Message> _messages;

    public ListAdapterForMassage(Context context) {
        _context = context;
        _messages = new Vector<>();
    }

    public Vector<Message> get_messages() {
        return _messages;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater lInflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = lInflater.inflate(R.layout.list_layout, parent, false);
        }
        ((TextView) convertView.findViewById(R.id.name)).setText(_messages.get(position).getName());
        ((TextView) convertView.findViewById(R.id.time)).setText(_messages.get(position).getDate());
        ((TextView) convertView.findViewById(R.id.massage)).setText(_messages.get(position).getMassege());
        return convertView;
    }
}
