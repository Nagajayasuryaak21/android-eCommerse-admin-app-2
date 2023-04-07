package com.surya.shoppingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class EditCourseActivity extends AppCompatActivity {

    private TextInputEditText courseName,coursePrice,courseSuitedFor,courseImage,courseLink,courseDesc;
    private Button UpdateCourse,DeleteCourse;
    private ProgressBar loadingPB;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private String CourseId;
    private CourseRVModal courseRVModal;
    private ImageView courseIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_course);
        courseName = findViewById(R.id.idEdtCourseName);
        coursePrice = findViewById(R.id.idEdtCoursePrice);
        courseSuitedFor=findViewById((R.id.idEdtCourseSuitedFor));
        courseDesc = findViewById(R.id.idEdtCourseDesc);
        courseImage = findViewById(R.id.idEdtCourseImageLink);
        courseLink = findViewById(R.id.idEdtCourseLink);
        UpdateCourse = findViewById(R.id.idBtnUpdateCourse);
        DeleteCourse = findViewById(R.id.idBtnDeleteCourse);
        loadingPB = findViewById(R.id.idPBLoading);
        courseIV = findViewById(R.id.idIVCourse);
        firebaseDatabase = FirebaseDatabase.getInstance();
        courseRVModal = getIntent().getParcelableExtra("course");
        if(courseRVModal!=null){
            courseName.setText(courseRVModal.getCourseName());
            coursePrice.setText(courseRVModal.getCoursePrice());
            courseSuitedFor.setText(courseRVModal.getCourseSuitedFor());
            courseDesc.setText(courseRVModal.getCourseDescription());
            courseImage.setText(courseRVModal.getCourseImg());
            courseLink.setText(courseRVModal.getCourseLink());
            CourseId = courseRVModal.getCourseID();
            Picasso.get().load(courseRVModal.getCourseImg()).into(courseIV);

        }

        databaseReference = firebaseDatabase.getReference("Courses").child(CourseId);
        UpdateCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingPB.setVisibility(View.VISIBLE);
                String cName = courseName.getText().toString();
                String cPrice = coursePrice.getText().toString();
                String cSuitFor = courseSuitedFor.getText().toString();
                String cDesc = courseDesc.getText().toString();
                String cImage = courseImage.getText().toString();
                String cLink = courseLink.getText().toString();
                //private String courseName ,  ,  , courseSuitedFor ,  , , ;

                //CourseRVModal UpdateCourseRVModal = new CourseRVModal(cName,cDesc,cPrice,cSuitFor,cImage,cLink,CourseId);
                Map <String,Object> map = new HashMap<>();
                map.put("courseName",cName);
                map.put("coursePrice",cPrice);
                map.put("courseSuitedFor",cSuitFor);
                map.put("courseDescription",cDesc);
                map.put("courseImg",cImage);
                map.put("courseLink",cLink);
                map.put("courseID",CourseId);

                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // to add all the data to our backend
                        databaseReference.updateChildren(map);
                        loadingPB.setVisibility(View.GONE);
                        Toast.makeText(EditCourseActivity.this, "Course Updated Successfully..", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(EditCourseActivity.this,MainActivity.class) );

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        loadingPB.setVisibility(View.GONE);
                        Toast.makeText(EditCourseActivity.this, "Error in Updating Data...", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
        DeleteCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCourse();
            }
        });
    }

    private void deleteCourse() {
        databaseReference.removeValue();
        Toast.makeText(EditCourseActivity.this, "Course Deleted Successfully...", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(EditCourseActivity.this,MainActivity.class));
        finish();
    }
}