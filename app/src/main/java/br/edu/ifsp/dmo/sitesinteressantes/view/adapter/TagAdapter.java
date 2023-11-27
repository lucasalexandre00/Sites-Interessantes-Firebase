package br.edu.ifsp.dmo.sitesinteressantes.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.edu.ifsp.dmo.sitesinteressantes.R;
import br.edu.ifsp.dmo.sitesinteressantes.model.TagSite;

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.ViewHolder> {
    private List<TagSite> tags;
    private ItemClick itemClick;

    public TagAdapter(List<TagSite> tags, ItemClick itemClick){
        this.tags = tags;
        this.itemClick = itemClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tag_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tagTextView.setText(tags.get(position).getTag());
        holder.itemView.setOnClickListener(view -> itemClick.clickAction(position));
    }

    @Override
    public int getItemCount() {
        return tags.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tagTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tagTextView = itemView.findViewById(R.id.textview_tag_item);
        }
    }
}
