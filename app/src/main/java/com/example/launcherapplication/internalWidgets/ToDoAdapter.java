package com.example.launcherapplication.internalWidgets;

import android.content.Context;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Toast;

import com.example.launcherapplication.R;

import java.util.ArrayList;
import java.util.List;

public class ToDoAdapter extends ArrayAdapter<Task> {
    Context context;
    List<Task> taskList=new ArrayList<Task>();
    int layoutResourceId;
    protected TaskerDbHelper db;


    public ToDoAdapter(Context context, int layoutResourceId, List<Task> objects, TaskerDbHelper db) {
        super(context, layoutResourceId, objects);
        this.layoutResourceId = layoutResourceId;
        this.taskList=objects;
        this.context=context;
        this.db=db;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CheckBox chk = null;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.fragment_to_do_internal_widget_item, parent, false);
            //CheckBox chk=(CheckBox)rowView.findViewById(R.id.chkStatus);
            chk=(CheckBox)convertView.findViewById(R.id.checkBox1);

            convertView.setTag(chk);
            chk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v;
                    Task changeTask = (Task) cb.getTag();
                    changeTask.setStatus(cb.isChecked() == true ? 1 : 0);
                    db.updateTask(changeTask);
                    Toast.makeText(getContext(),"Clicked on Checkbox: " + cb.getText() + " is "+ cb.isChecked(), Toast.LENGTH_LONG).show();
                }
            });
        } else {
            chk = (CheckBox) convertView.getTag();
        }

        Task current = taskList.get(position);
        chk.setText(current.getTaskName());
        chk.setChecked(current.getStatus() == 1 ? true : false);
        chk.setTag(current);
        Log.d("listener", String.valueOf(current.getId()));
        return convertView;
    }
}
