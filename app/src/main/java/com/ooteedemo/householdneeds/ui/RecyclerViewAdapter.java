package com.ooteedemo.householdneeds.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import com.ooteedemo.householdneeds.R;
import com.ooteedemo.householdneeds.data.DatabaseHandler;
import com.ooteedemo.householdneeds.model.Item;

import java.text.MessageFormat;
import java.util.List;
import java.util.zip.Inflater;

public class RecyclerViewAdapter extends Adapter<RecyclerViewAdapter.ViewHolder>{

    private Context context;
    private List<Item> itemList;

    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private LayoutInflater inflater;

    public RecyclerViewAdapter(Context context, List<Item> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_row,viewGroup,false);
        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder viewHolder, int position) {
        Item item = itemList.get(position); // Item object
        viewHolder.itemName.setText(MessageFormat.format("Item: {0}", item.getItemName()));
        viewHolder.itemColor.setText(MessageFormat.format("Color: {0}", item.getItemColor()));
        viewHolder.quantity.setText(MessageFormat.format("Quantity: +{0}", String.valueOf(item.getItemQuantity())));
        viewHolder.size.setText(MessageFormat.format("Size: {0}", String.valueOf(item.getItemSize())));
        viewHolder.dateAdded.setText(MessageFormat.format("Added On: {0}", item.getDateItemAdded()));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView itemName;
        public TextView itemColor;
        public TextView quantity;
        public TextView size;
        public TextView dateAdded;
        public Button editButton;
        public Button deleteButton;
        public int id;

        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            context = ctx;

            itemName = itemView.findViewById(R.id.item_name);
            itemColor = itemView.findViewById(R.id.item_color);
            quantity = itemView.findViewById(R.id.item_quantity);
            size = itemView.findViewById(R.id.item_size);
            dateAdded = itemView.findViewById(R.id.item_date);

            editButton = itemView.findViewById(R.id.edit_button);
            deleteButton = itemView.findViewById(R.id.delete_button);
            editButton.setOnClickListener(this);
            deleteButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position;
            switch (v.getId()) {
                case R.id.edit_button:
//                    do edit item stuff
                    break;
                case R.id.delete_button:
                    position = getAdapterPosition();
                    Item item = itemList.get(position);
                    deleteItem(item);
                    break;
            }
        }

        private void deleteItem(Item id) {

            builder = new AlertDialog.Builder(context);
            inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.confirmation_pop,null);

            Button noButton = view.findViewById(R.id.conf_no_button);
            Button yesButton = view.findViewById(R.id.conf_yes_button);

            builder.setView(view);
            dialog = builder.create();
            dialog.show();

            noButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            yesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseHandler db = new DatabaseHandler(context);
                    db.deleteItem(id);
                    itemList.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());

                    dialog.dismiss();
                }
            });
        }
    }
}
