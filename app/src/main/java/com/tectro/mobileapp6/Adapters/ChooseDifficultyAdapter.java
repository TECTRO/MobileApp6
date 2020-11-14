package com.tectro.mobileapp6.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tectro.mobileapp6.Models.Support.Enums.EDifficulty;
import com.tectro.mobileapp6.R;

import java.util.function.Consumer;


public class ChooseDifficultyAdapter extends RecyclerView.Adapter<ChooseDifficultyAdapter.ViewHolder>  {
    private LayoutInflater inflater;
    private Consumer<Integer> ClickFunction;

    public ChooseDifficultyAdapter(Context context, Consumer<Integer> clickFunction) {
        inflater=LayoutInflater.from(context);
        ClickFunction = clickFunction;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_button_layout, parent, false);
        ViewHolder holder = new ViewHolder(view);
        holder.button.setOnClickListener(v ->
        {
            int pos = holder.getAdapterPosition();
            if(pos!= RecyclerView.NO_POSITION && ClickFunction!=null)
                ClickFunction.accept(pos);
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.button.setText(String.join(" ", EDifficulty.values()[position].name().split("_")));
    }

    @Override
    public int getItemCount() {
        return EDifficulty.values().length;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        Button button;

        ViewHolder(View view) {
            super(view);
            button = view.findViewById(R.id.difficultyButton);
        }
    }
}
