package com.surya.shoppingapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements CourseRVAdapter.CourseClickInterface {

    private RecyclerView courseRV;
    private ProgressBar loadingPB;
    private FloatingActionButton addFAB;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private RelativeLayout bottomSheet;
    private ArrayList<CourseRVModal> courseRVModalArrayList;
    private CourseRVAdapter courseRVAdapter;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        courseRV = findViewById(R.id.idRVCourses);
        loadingPB = findViewById(R.id.idPBLoading);
        addFAB = findViewById(R.id.idBtnAddCourseAB);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference  = firebaseDatabase.getReference("Courses");
        courseRVModalArrayList = new ArrayList<>();
        bottomSheet = findViewById(R.id.idRLBSheet);
        mAuth = FirebaseAuth.getInstance();
        courseRVAdapter = new CourseRVAdapter(courseRVModalArrayList,this,this);
        courseRV.setLayoutManager(new LinearLayoutManager(this));
        courseRV.setAdapter(courseRVAdapter);
        addFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,AddCourseActivity.class));
            }
        });
        getAllCourses();
    }

    private void getAllCourses(){
        courseRVModalArrayList.clear();
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                loadingPB.setVisibility(View.GONE);
                courseRVModalArrayList.add(snapshot.getValue(CourseRVModal.class));
                courseRVAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                loadingPB.setVisibility(View.GONE);
                courseRVAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                loadingPB.setVisibility(View.GONE);
                courseRVAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                loadingPB.setVisibility(View.GONE);
                courseRVAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onCourceClick(int position) {
        displayBottomSheet(courseRVModalArrayList.get(position));

    }
    private void displayBottomSheet(CourseRVModal courseRVModal){
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View layout = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_dialog,bottomSheet);
        bottomSheetDialog.setContentView(layout);
        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.setCanceledOnTouchOutside(true);
        bottomSheetDialog.show();

        TextView courseNameTV = layout.findViewById(R.id.idTVCourseName);
        TextView courseDescTV = layout.findViewById(R.id.idTVDescription);
        TextView courseSuitedForTV = layout.findViewById(R.id.idTVSuitedFor);
        TextView coursePrice = layout.findViewById(R.id.idTVPrice);
        ImageView courseIV = layout.findViewById((R.id.idIVCourse));
        Button editBtn = layout.findViewById(R.id.idBtnEdit);
        Button viewDetailsBtn = layout.findViewById(R.id.idViewDetails);

        courseNameTV.setText(courseRVModal.getCourseName());
        coursePrice.setText("Rs. "+courseRVModal.getCoursePrice());
        courseDescTV.setText(courseRVModal.getCourseDescription());
        courseSuitedForTV.setText(courseRVModal.getCourseSuitedFor());
        Picasso.get().load(courseRVModal.getCourseImg()).into(courseIV);

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,EditCourseActivity.class);
                i.putExtra("course",courseRVModal);
                startActivity(i);
            }
        });

        viewDetailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(courseRVModal.getCourseLink()));
                startActivity(i);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        //Toast.makeText(this, "User Logged Out pressed", Toast.LENGTH_SHORT).show();
        switch (id){
            case R.id.idLogOut:
                Toast.makeText(this, "User Logged Out", Toast.LENGTH_SHORT).show();
                mAuth.signOut();
                Intent i = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(i);
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}