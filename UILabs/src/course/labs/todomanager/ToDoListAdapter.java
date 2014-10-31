package course.labs.todomanager;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.CompoundButton.OnCheckedChangeListener;
import org.w3c.dom.Text;

public class ToDoListAdapter extends BaseAdapter {

	private final List<ToDoItem> mItems = new ArrayList<ToDoItem>();
    private final LayoutInflater inflater;

	private static final String TAG = "Lab-UserInterface";
    private ArrayAdapter<CharSequence> mSpinnerAdapter;

    public ToDoListAdapter(Context context) {

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mSpinnerAdapter = ArrayAdapter.createFromResource(context,
                R.array.priorities, android.R.layout.simple_spinner_item);
        mSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	}

	// Add a ToDoItem to the adapter
	// Notify observers that the data set has changed

	public void add(ToDoItem item) {

		mItems.add(item);
		notifyDataSetChanged();

	}

	// Clears the list adapter of all items.

	public void clear() {

		mItems.clear();
		notifyDataSetChanged();

	}

	// Returns the number of ToDoItems

	@Override
	public int getCount() {

		return mItems.size();

	}

	// Retrieve the number of ToDoItems

	@Override
	public Object getItem(int pos) {

		return mItems.get(pos);

	}

	// Get the ID for the ToDoItem
	// In this case it's just the position

	@Override
	public long getItemId(int pos) {

		return pos;

	}

	// Create a View for the ToDoItem at specified position
	// Remember to check whether convertView holds an already allocated View
	// before created a new View.
	// Consider using the ViewHolder pattern to make scrolling more efficient
	// See: http://developer.android.com/training/improving-layouts/smooth-scrolling.html
	private static class ViewHolder {
        TextView titleView;
        CheckBox statusView;
        Spinner priorityView;
        TextView dateView;
    }

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

		if (convertView == null) {

            convertView = inflater.inflate(R.layout.todo_item, parent, false);
            holder = new ViewHolder();
            holder.titleView = (TextView) convertView.findViewById(R.id.titleView);
            holder.statusView = (CheckBox) convertView.findViewById(R.id.statusCheckBox);
            holder.priorityView = (Spinner) convertView.findViewById(R.id.priorityView);
            holder.priorityView.setAdapter(mSpinnerAdapter);
            holder.dateView = (TextView) convertView.findViewById(R.id.dateView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final ToDoItem toDoItem = mItems.get(position);

        holder.titleView.setText(toDoItem.getTitle());

        final CheckBox statusView = holder.statusView;
        ToDoItem.Status status = toDoItem.getStatus();
        if (status == ToDoItem.Status.DONE) {
            holder.statusView.setChecked(true);
        } else {
            holder.statusView.setChecked(false);
        }

        statusView.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                Log.i(TAG, "Entered onCheckedChanged()");

                if (statusView.isChecked()) {
                    toDoItem.setStatus(ToDoItem.Status.DONE);
                } else {
                    toDoItem.setStatus(ToDoItem.Status.NOTDONE);
                }
            }
        });


        holder.dateView.setText(ToDoItem.FORMAT.format(toDoItem.getDate()));
        holder.priorityView.setSelection(toDoItem.getPriority().ordinal());

        holder.priorityView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                toDoItem.setPriority(ToDoItem.Priority.values()[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d(TAG, "nothing selected from spinner listener");
            }
        });

        // Return the View you just created
		return convertView;

	}
}
