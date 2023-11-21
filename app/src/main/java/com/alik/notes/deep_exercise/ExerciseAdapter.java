package com.alik.notes.deep_exercise;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alik.notes.R;

import java.util.ArrayList;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder> {

    private ArrayList<Exercise> exercises;
    private OnExerciseClickListener onExerciseClickListener;

    public ExerciseAdapter(ArrayList<Exercise> exercises) {
        this.exercises = exercises;
    }

    interface OnExerciseClickListener {
        void onExerciseClick(int position);
    }

    public void setOnExerciseClickListener(OnExerciseClickListener onExerciseClickListener) {
        this.onExerciseClickListener = onExerciseClickListener;
    }

    private TextView textViewDescriptionExAct;
    private TextView textViewTitleExAct;

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.exercise_item, viewGroup, false);
        return new ExerciseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder exerciseViewHolder, int i) {
        Exercise exercise = exercises.get(i);
        exerciseViewHolder.textViewTitleExAct.setText(exercise.getTitle());
        exerciseViewHolder.textViewDescriptionExAct.setText(exercise.getDescription());
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    class ExerciseViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewDescriptionExAct;
        private TextView textViewTitleExAct;

        public ExerciseViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitleExAct = itemView.findViewById(R.id.textViewTitleExAct);
            textViewDescriptionExAct = itemView.findViewById(R.id.textViewDescriptionExAct);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onExerciseClickListener != null) {
                        onExerciseClickListener.onExerciseClick(getAdapterPosition());
                    }
                }
            });
        }
    }
}
