package com.example.proj3_4;

import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter<String> adapter;
    private Set<Integer> selectedPositions = new HashSet<>();
    private ActionMode actionMode;
    private ActionMode.Callback actionModeCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);

        ArrayList<String> items = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            items.add("Item " + i);
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_activated_1, items);
        listView.setAdapter(adapter);

        // 设置长按监听器启动 ActionMode
        listView.setOnItemLongClickListener((AdapterView<?> parent, android.view.View view, int position, long id) -> {
            if (actionMode != null) {
                return false;
            }

            actionMode = startActionMode(actionModeCallback);
            selectItem(position);
            return true;
        });


        listView.setOnItemClickListener((parent, view, position, id) -> {
            if (actionMode != null) {
                selectItem(position);
            }
        });


        actionModeCallback = new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.context_menu, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }
            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                if (item.getItemId() == R.id.action_delete) {
                    deleteSelectedItems();
                    mode.finish();
                    return true;
                }
                return false;
            }


            @Override
            public void onDestroyActionMode(ActionMode mode) {
                actionMode = null;
                selectedPositions.clear();
                adapter.notifyDataSetChanged();
            }
        };
    }

    private void selectItem(int position) {
        if (selectedPositions.contains(position)) {
            selectedPositions.remove(position);
        } else {
            selectedPositions.add(position);
        }


        listView.setItemChecked(position, selectedPositions.contains(position));


        if (actionMode != null) {
            actionMode.setTitle(selectedPositions.size() + " selected");
        }

        if (selectedPositions.isEmpty() && actionMode != null) {
            actionMode.finish();
        }
    }

    private void deleteSelectedItems() {
        ArrayList<String> itemsToRemove = new ArrayList<>();
        for (int position : selectedPositions) {
            itemsToRemove.add(adapter.getItem(position));
        }

        for (String item : itemsToRemove) {
            adapter.remove(item);
        }

        Toast.makeText(this, "Selected items deleted", Toast.LENGTH_SHORT).show();
    }

}
