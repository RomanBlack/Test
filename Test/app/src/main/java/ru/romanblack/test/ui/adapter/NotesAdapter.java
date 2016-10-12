package ru.romanblack.test.ui.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashSet;
import java.util.Set;

import nl.qbusict.cupboard.CupboardFactory;
import ru.romanblack.test.R;
import ru.romanblack.test.data.entities.Note;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {

    public NotesAdapter(Cursor cursor) {
        this.cursor = cursor;
    }

    private Cursor cursor;

    private AdapterInterface adapterInterface;

    private Set<Long> selected = new HashSet<>();

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        View view = LayoutInflater.from(context).inflate(R.layout.item_note, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Note note = getItem(position);

        holder.titleView.setText(note.getTitle());
        holder.rootView.setTag(position);
        holder.rightIconView.setTag(position);

        holder.rootView.setSelected(selected.contains(note.getId()));
    }

    @Override
    public int getItemCount() {
        return cursor == null ? 0 : cursor.getCount();
    }

    private Note getItem(int position) {
        if (cursor == null) {
            return null;
        }

        cursor.moveToPosition(position);

        return CupboardFactory.cupboard()
                .withCursor(cursor)
                .get(Note.class);
    }

    public void setAdapterInterface(AdapterInterface adapterInterface) {
        this.adapterInterface = adapterInterface;
    }

    public void onRemoved(Note note) {
        if (selected.contains(note.getId())) {
            selected.remove(note.getId());
        }
    }

    public void swapCursor(Cursor newCursor) {
        this.cursor = newCursor;

        notifyDataSetChanged();
    }

    class ItemClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Integer tag = (Integer) v.getTag();

            if (tag != null && adapterInterface != null) {
                adapterInterface.onItemClick(v, tag, getItem(tag));
            }
        }
    }

    class ItemLongClickListener implements View.OnLongClickListener {

        @Override
        public boolean onLongClick(View v) {
            Integer tag = (Integer) v.getTag();

            if (tag != null && adapterInterface != null) {
                adapterInterface.onItemLongClick(v, tag, getItem(tag));
            }

            return true;
        }
    }

    class CheckClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Integer tag = (Integer) v.getTag();

            if (tag != null) {
                Note note = getItem(tag);

                if (selected.contains(note.getId())) {
                    selected.remove(note.getId());
                } else {
                    selected.add(note.getId());
                }
            }

            notifyDataSetChanged();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);

            rootView = itemView;
            rootView.setOnClickListener(new ItemClickListener());
            rootView.setOnLongClickListener(new ItemLongClickListener());

            iconView = (ImageView) itemView.findViewById(R.id.icon);
            titleView = (TextView) itemView.findViewById(R.id.title);

            rightIconView = (ImageView) itemView.findViewById(R.id.right);
            rightIconView.setOnClickListener(new CheckClickListener());
        }

        private View rootView;
        private ImageView iconView;
        private TextView titleView;
        private ImageView rightIconView;
    }

    public interface AdapterInterface {

        public void onItemClick(View view, int position, Note item);

        public void onItemLongClick(View view, int position, Note item);
    }
}
