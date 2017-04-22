package com.example.nimolee.chatclient;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Vector;

class ListAdapterForSelectUser extends BaseAdapter {

    private Context _context;
    private Vector<Integer> _userID = new Vector<>();
    private Vector<String> _users = new Vector<>();
    private Vector<Boolean> _useUser = new Vector<>();
    private DataOutputStream _DOS;
    private String out;

    void addUser(int id, String name) {
        if (!_userID.contains(id)) {
            _userID.add(id);
            _users.add(name);
            _useUser.add(false);
        }
    }

    ListAdapterForSelectUser(Context context, DataOutputStream Dos) {
        _context = context;
        _DOS = Dos;
    }

    @Override
    public int getCount() {
        return _users.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.select_user_list_layout, parent, false);
        }
        ((CheckBox) convertView.findViewById(R.id.check_user)).setChecked(_useUser.get(position));
        ((CheckBox) convertView.findViewById(R.id.check_user)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                _useUser.set(position, !_useUser.get(position));
                out = "t";
                for (int i = 0; i < _userID.size(); i++) {
                    if (_useUser.get(i)) {
                        out += "\n" + _userID.get(i);
                    }

                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            _DOS.writeUTF(out);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

            }
        });
        ((TextView) convertView.findViewById(R.id.username)).setText(_users.get(position));
        return convertView;
    }
}
