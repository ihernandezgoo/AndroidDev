package com.example.va_projects; // Make sure this package name is correct for your project

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder> {

    private List<Project> projectsList;

    // Constructor to initialize the adapter with the list of projects
    public ProjectAdapter(List<Project> projectsList) {
        this.projectsList = projectsList;
    }

    @NonNull
    @Override
    public ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each project item
        // This assumes you have a layout file named "item_project.xml" in your res/layout folder
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_project, parent, false);
        return new ProjectViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectViewHolder holder, int position) {
        // Get the project at the current position
        Project currentProject = projectsList.get(position);

        // Bind the project data to the views in the ViewHolder
        holder.projectTitle.setText(currentProject.name);
        holder.projectDescription.setText(currentProject.description);
        holder.projectImage.setImageResource(currentProject.image);
    }

    @Override
    public int getItemCount() {
        // Return the total number of projects in the list
        if (projectsList == null) {
            return 0;
        }
        return projectsList.size();
    }

    /**
     * ViewHolder class to hold references to the views for each project item.
     * This improves performance by avoiding repeated calls to findViewById.
     */
    public static class ProjectViewHolder extends RecyclerView.ViewHolder {
        public ImageView projectImage;
        public TextView projectTitle;
        public TextView projectDescription;

        public ProjectViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize the views from the item_project.xml layout
            // Make sure the IDs here match the IDs in your item_project.xml file
            projectImage = itemView.findViewById(R.id.image_view_project_icon); // Example ID
            projectTitle = itemView.findViewById(R.id.text_view_project_title);     // Example ID
            projectDescription = itemView.findViewById(R.id.text_view_project_description); // Example ID
        }
    }
}