package com.sumin.notes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class WorkoutAdapter extends RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder>  {

    private ArrayList<Workout> workouts;
    private OnWorkoutClickListener onWorkoutClickListener;

    public WorkoutAdapter(ArrayList<Workout> workouts) {
        this.workouts = workouts;
    }

    public interface ItemTouchHelperAdapter {
        void onItemMove(int fromPosition, int toPosition);
        void onItemDismiss(int position);
    }

    interface OnWorkoutClickListener{
        void onWorkoutClick(int position);
        void onLongWorkoutClick  (int position);
    }

    public void setOnWorkoutClickListener(OnWorkoutClickListener onWorkoutClickListener) {
        this.onWorkoutClickListener = onWorkoutClickListener;
    }

    @NonNull
    @Override
    public WorkoutViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.workout_item,viewGroup,false);
        return new WorkoutViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutViewHolder workoutViewHolder, int i)
    //принимает эл. массива и устанавливает
    {
        Workout workout=workouts.get(i);
        workoutViewHolder.textViewTitle.setText(workout.getTitle());
        workoutViewHolder.textViewReps.setText(""+workout.getReps());
        workoutViewHolder.textViewWeight.setText(""+workout.getWeight());
        workoutViewHolder.textViewSet.setText(String.format("%s",workout.getSet()));
        workoutViewHolder.textViewDescription.setText(workout.getDescription());


    }

    @Override
    public int getItemCount() {
        return workouts.size();
    }

    class WorkoutViewHolder extends RecyclerView.ViewHolder{

        private TextView textViewTitle;
        private TextView textViewReps;
        private TextView textViewSet;
        private TextView textViewWeight;
        private TextView textViewDescription;

        public WorkoutViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle=itemView.findViewById(R.id.textViewTitleExAct);
            textViewDescription=itemView.findViewById(R.id.textViewDescriptionExAct);
            textViewSet=itemView.findViewById(R.id.textViewSet);
            textViewWeight=itemView.findViewById(R.id.textViewWeight);
            textViewReps=itemView.findViewById(R.id.textViewReps);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onWorkoutClickListener != null){
                        onWorkoutClickListener.onWorkoutClick(workouts.get(getAdapterPosition()).getId());
                    }
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onWorkoutClickListener != null){
                        onWorkoutClickListener.onLongWorkoutClick(getAdapterPosition());
                    }
                    return true;
                    //если не укажем тру то сработают два метода лонг и просто
                }
            });
        }
    }
}
