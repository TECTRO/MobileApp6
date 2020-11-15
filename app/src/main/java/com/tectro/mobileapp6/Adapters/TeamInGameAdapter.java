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
import com.tectro.mobileapp6.Models.Support.DataModels.GamePlayer;
import com.tectro.mobileapp6.Models.Support.DataModels.Hero;
import com.tectro.mobileapp6.Models.Support.Interfaces.ICollectionProvider;
import com.tectro.mobileapp6.R;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class TeamInGameAdapter extends RecyclerView.Adapter<TeamInGameAdapter.ViewHolder> implements ICollectionProvider<List<Hero>> {

    private final LayoutInflater inflater;
    private Consumer<Integer> onClick;
    private List<Hero> heroes;
    private boolean isChoiceAccessibilityLocked;

    public TeamInGameAdapter(Context context, Consumer<Integer> onClick) {
        inflater = LayoutInflater.from(context);
        this.onClick = onClick;
        heroes = new ArrayList<>();
        isChoiceAccessibilityLocked = true;
    }

    public void setChoiceAccessibility(boolean value)
    {
        isChoiceAccessibilityLocked = value;
        for (int i = 0; i < getItemCount(); i++)
            notifyItemChanged(i);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_team_in_game_layout, parent, false);
        ViewHolder holder = new ViewHolder(view);

        holder.selectHero.setOnClickListener(v ->
        {
            int pos = holder.getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION) {
                onClick.accept(pos);
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

        holder.selectHero.setVisibility(isChoiceAccessibilityLocked ? View.GONE : View.VISIBLE);
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
        Button selectHero;

        ViewHolder(View view) {
            super(view);
            HeroName = view.findViewById(R.id.HeroName);
            HeroWeaknesses = view.findViewById(R.id.heroWeaknesses);
            selectHero = view.findViewById(R.id.selectHero);
        }
    }
}
