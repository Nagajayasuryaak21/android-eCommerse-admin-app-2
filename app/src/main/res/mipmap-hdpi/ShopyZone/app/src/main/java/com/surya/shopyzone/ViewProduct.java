package com.surya.shopyzone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
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

import android.os.Bundle;

public class ViewProduct extends AppCompatActivity {

    private TextInputEditText courseName,coursePrice,courseSuitedFor,courseImage,courseLink,courseDesc;
    private Button UpdateCourse,Buybtn;
    private ProgressBar loadingPB;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference , databaseReferencePlaceOrder;
    private String CourseId,UserName;
    private CourseRVModal courseRVModal;
    private ImageView courseIV;
    private int k=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_product);
        courseName = findViewById(R.id.idEdtCourseName);
        coursePrice = findViewById(R.id.idEdtCoursePrice);
        courseSuitedFor=findViewById((R.id.idEdtCourseSuitedFor));
        courseDesc = findViewById(R.id.idEdtCourseDesc);
        courseLink = findViewById(R.id.idEdtCourseLink);
        courseIV=findViewById(R.id.idIVCourse);
        //UpdateCourse = findViewById(R.id.idBtnUpdateCourse);
        Buybtn = findViewById(R.id.idBtnDeleteCourse);
        loadingPB = findViewById(R.id.idPBLoading);
        firebaseDatabase = FirebaseDatabase.getInstance();
        courseRVModal = getIntent().getParcelableExtra("course");
        UserName = getIntent().getStringExtra("user");
        if(courseRVModal!=null){
            courseName.setText(courseRVModal.getCourseName());
            coursePrice.setText(courseRVModal.getCoursePrice());
            courseSuitedFor.setText(courseRVModal.getCourseSuitedFor());
            courseDesc.setText(courseRVModal.getCourseDescription());
            courseLink.setText(courseRVModal.getCourseLink());
            CourseId = courseRVModal.getCourseID();
            Picasso.get().load(courseRVModal.getCourseImg()).into(courseIV);

        }

        //databaseReference = firebaseDatabase.getReference("Courses").child(CourseId);
        databaseReferencePlaceOrder = firebaseDatabase.getReference(UserName);
        Buybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //deleteCourse();
                loadingPB.setVisibility(View.VISIBLE);
//                Intent i = new Intent(Intent.ACTION_VIEW);
//                i.setData(Uri.parse(courseRVModal.getCourseLink()));
//                startActivity(i);
                k=1;
                //Toast.makeText(ViewProduct.this, "Clicked", Toast.LENGTH_SHORT).show();
                PlaceOrder newOrder = new PlaceOrder(courseRVModal.getCourseName());
                databaseReferencePlaceOrder.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(k==1) {
                            databaseReferencePlaceOrder.child(courseRVModal.getCourseName()).setValue(newOrder);
                            loadingPB.setVisibility(View.GONE);
                            Toast.makeText(ViewProduct.this, "Placed Order successfully...!", Toast.LENGTH_SHORT).show();
                            k=0;
                            Intent i = new Intent(ViewProduct.this,MainActivity.class);
                            i.putExtra("user",UserName);
                            startActivity(i);
                            finish();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        loadingPB.setVisibility(View.GONE);
                        Toast.makeText(ViewProduct.this, "Error in Placing Order...", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
    }

    private void deleteCourse() {
        databaseReference.removeValue();
        Toast.makeText(ViewProduct.this, "Course Deleted Successfully...", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(ViewProduct.this,MainActivity.class));
        finish();
    }


}