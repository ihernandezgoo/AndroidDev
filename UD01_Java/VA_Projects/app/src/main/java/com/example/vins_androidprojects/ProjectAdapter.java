package com.example.vins_androidprojects;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public abstract class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder> {

    private Project[] projects;

    public ProjectAdapter (Project[] projects) {
        this.projects = projects;
    }
    static class ProjectViewHolder extends RecyclerView.ViewHolder {
        public ProjectViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }


}