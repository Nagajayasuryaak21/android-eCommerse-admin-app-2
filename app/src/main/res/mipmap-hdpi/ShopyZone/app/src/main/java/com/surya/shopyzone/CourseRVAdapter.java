package com.surya.shopyzone;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CourseRVAdapter extends RecyclerView.Adapter<CourseRVAdapter.ViewHolder> {

    int lastPos =-1;
    ArrayList<CourseRVModal> courseRVModalArrayList;
    private Context context;
    private CourseClickInterface courseClickInterface;


    public CourseRVAdapter(ArrayList<CourseRVModal> courseRVModalArrayList, Context context, CourseClickInterface courseClickInterface) {
        this.courseRVModalArrayList = courseRVModalArrayList;
        this.context = context;
        this.courseClickInterface = courseClickInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.course_rv_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,int pos) {
        pos = holder.getAdapterPosition();
        CourseRVModal courseRVModal = courseRVModalArrayList.get(pos);
        holder.courseNameTV.setText((courseRVModal.getCourseName()));
        holder.CoursePriceTV.setText((courseRVModal.getCoursePrice()));
        Picasso.get().load(courseRVModal.getCourseImg()).into(holder.courseIV);
        setAnimation(holder.itemView,pos);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                courseClickInterface.onCourceClick(holder.getAdapterPosition());
            }
        });

    }

    private void setAnimation(View itemView, int position) {
        if(position>lastPos){
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            itemView.setAnimation(animation);
            lastPos=position;
        }
    }


    @Override
    public int getItemCount() {
        return courseRVModalArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView courseNameTV , CoursePriceTV;
        private ImageView courseIV;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            courseNameTV = itemView.findViewById(R.id.idTVCourseName);
            CoursePriceTV = itemView.findViewById(R.id.idTVPrice);
            courseIV = itemView.findViewById(R.id.idIVCourse);
        }
    }

    public interface CourseClickInterface{
        void onCourceClick(int position);

    }

}
