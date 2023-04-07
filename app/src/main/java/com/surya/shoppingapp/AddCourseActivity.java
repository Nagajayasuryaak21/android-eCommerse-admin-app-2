package com.surya.shoppingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddCourseActivity extends AppCompatActivity {

    private TextInputEditText courseName,coursePrice,courseSuitedFor,courseImage,courseLink,courseDesc;
    private Button AddCourse;
    private ProgressBar lodingPB;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private String CourseId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);
        courseName = findViewById(R.id.idEdtCourseName);
        coursePrice = findViewById(R.id.idEdtCoursePrice);
        courseSuitedFor=findViewById((R.id.idEdtCourseSuitedFor));
        courseDesc = findViewById(R.id.idEdtCourseDesc);
        courseImage = findViewById(R.id.idEdtCourseImageLink);
        courseLink = findViewById(R.id.idEdtCourseLink);
        AddCourse = findViewById(R.id.idBtnAddCourse);
        lodingPB = findViewById(R.id.idPBLoading);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Courses");

        AddCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lodingPB.setVisibility(View.VISIBLE);
                String cName = courseName.getText().toString();
                String cPrice = coursePrice.getText().toString();
                String cSuitFor = courseSuitedFor.getText().toString();
                String cDesc = courseDesc.getText().toString();
                String cImage = courseImage.getText().toString();
                String cLink = courseLink.getText().toString();
                CourseId = cName;
                CourseRVModal courseRVModal = new CourseRVModal(cName,cDesc,cPrice,cSuitFor,cImage,cLink,CourseId);
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // to add all the data to our backend
                        databaseReference.child(CourseId).setValue(courseRVModal);
                        lodingPB.setVisibility(View.GONE);
                        Toast.makeText(AddCourseActivity.this, "Course Added Successfully..", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AddCourseActivity.this,MainActivity.class) );

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        lodingPB.setVisibility(View.GONE);
                        Toast.makeText(AddCourseActivity.this, "Error in Adding Data...", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });



    }
}