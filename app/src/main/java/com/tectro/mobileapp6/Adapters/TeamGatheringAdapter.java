package com.tectro.mobileapp6.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tectro.mobileapp6.Models.Support.DataModels.GameClasses.EntityClass;
import com.tectro.mobileapp6.Models.Support.DataModels.Hero;
import com.tectro.mobileapp6.Models.Support.Interfaces.ICollectionProvider;
import com.tectro.mobileapp6.R;

import java.util.ArrayList;
import java.util.List;

public class TeamGatheringAdapter extends RecyclerView.Adapter<TeamGatheringAdapter.ViewHolder> implements ICollectionProvider<List<Hero>> {

    private final LayoutInflater inflater;
    List<Hero> heroes;

    public TeamGatheringAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        heroes = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_team_layout, parent, false);
        ViewHolder holder = new ViewHolder(view);
        holder.next.setOnClickListener(v ->
        {
            int pos = holder.getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION) {
                heroes.get(pos).ChangeClassNext();
                notifyItemChanged(pos);
            }
        });

        holder.previous.setOnClickListener(v ->
        {
            int pos = holder.getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION) {
                heroes.get(pos).ChangeClassPrevious();
                notifyItemChanged(pos);
            }
        });

        holder.remove.setOnClickListener(v ->
        {
            int pos = holder.getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION) {
                heroes.remove(pos);
                notifyItemRemoved(pos);
                //notifyDataSetChanged();
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EntityClass heroClass = heroes.get(position).getHClass();
        holder.HeroName.setText(heroClass.getClassName().name());

        String[] weaknesses = new String[heroClass.getWeaknessCount()];
        for (int i = 0; i < heroClass.getWeaknessCount(); i++)
            weaknesses[i] = heroClass.getWeakness(i).name();

        holder.HeroWeaknesses.setText(String.join("\n", weaknesses));
    }

    @Override
    public int getItemCount() {
        return heroes.size();
    }

    @Override
    public void setCollection(List<Hero> collection) {
        heroes = collection;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView HeroName;
        TextView HeroWeaknesses;
        Button previous;
        Button next;
        Button remove;

        ViewHolder(View view) {
            super(view);
            HeroName = view.findViewById(R.id.HeroName);
            HeroWeaknesses = view.findViewById(R.id.heroWeaknesses);
            previous = view.findViewById(R.id.previousClass);
            next = view.findViewById(R.id.nextClass);
            remove = view.findViewById(R.id.removeHero);
        }
    }

}
