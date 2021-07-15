package com.example.launcherapplication.internalWidgets;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.launcherapplication.R;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class ToDoInternalWidget extends Fragment {

    protected TaskerDbHelper db;
    List<Task> list;
    ToDoAdapter adapt;

    public ToDoInternalWidget() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       // getActivity().setContentView(R.layout.fragment_to_do_internal_widget);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_to_do_internal_widget, container, false);

        //db is a variable of type TaskerDbHelper
        db = new TaskerDbHelper(getContext());
        list=db.getAllTasks();
        adapt = new ToDoAdapter(getContext(),R.layout.fragment_to_do_internal_widget_item , list, db);
        Log.d("dbTasks",list.get(1).getTaskName());
        ToDoListView listTask = view.findViewById(R.id.listView1);
        listTask.setAdapter(adapt);

        // Inflate the layout for this fragment

        TextView button = view.findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText t = getActivity().findViewById(R.id.editText1);
                String s = t.getText().toString();
                Log.d("ToDoButtonPressed", s);
                if (s.equalsIgnoreCase("")) {
                    Toast.makeText(getContext(), "enter the task description first!!", Toast.LENGTH_LONG).show();
                } else {
                    Task task = new Task(s, 0);
                    db.addTask(task);
                    Log.d("tasker", "data added");
                    t.setText("");
                    adapt.add(task);
                    adapt.notifyDataSetChanged();
                }
            }
        });

        return view;
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getActivity().getMenuInflater().inflate(R.menu.activity_view_task, menu);
//        return true;
//    }

}