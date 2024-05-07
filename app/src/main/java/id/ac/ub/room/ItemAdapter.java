package id.ac.ub.room;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    private List<Item> itemList;

    public ItemAdapter(List<Item> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item item = itemList.get(position);
        holder.textViewItem.setText(item.getJudul());

        holder.buttonHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        Context context = holder.itemView.getContext();
                        AppDatabase.getInstance(context).itemDao().delete(itemList.get(position));

                        final List<Item> updatedList = AppDatabase.getInstance(context).itemDao().getAll();

                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateData(updatedList);
                            }
                        });
                    }
                });
            }
        });
    }


    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        Button buttonHapus;
        TextView textViewItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewItem = itemView.findViewById(R.id.textViewItem);
            buttonHapus = itemView.findViewById(R.id.buttonHapus);

        }
    }

    public void updateData(List<Item> newData) {
        itemList.clear();
        itemList.addAll(newData);
        notifyDataSetChanged();
    }
}