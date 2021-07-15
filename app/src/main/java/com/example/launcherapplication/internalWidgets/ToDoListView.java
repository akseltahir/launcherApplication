package com.example.launcherapplication.internalWidgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class ToDoListView extends ListView {

    public ToDoListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ToDoListView(Context context) {
        super(context);
    }

    public ToDoListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
