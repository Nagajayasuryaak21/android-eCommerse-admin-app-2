package com.surya.shopyzone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
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
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyOrders extends AppCompatActivity implements CourseRVAdapter.CourseClickInterface {

    private RecyclerView courseRV;
    private ProgressBar loadingPB;
    private FloatingActionButton addFAB;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference,databaseReferencecm;
    private RelativeLayout bottomSheet;
    private CourseRVAdapter courseRVAdapter;
    private ArrayList<CourseRVModal> courseRVModalArrayList;
    private ArrayList<PlaceOrder> placeOrders;
    private Map<String,CourseRVModal> map;
    private FirebaseAuth mAuth;
    private String mystring="",Username;
    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);
        Intent intent = getIntent();
        map = (HashMap<String, CourseRVModal>) intent.getSerializableExtra("map");
        Username = intent.getStringExtra("user");
        courseRV = findViewById(R.id.idRVCourses);
        loadingPB = findViewById(R.id.idPBLoading);
        //addFAB = findViewById(R.id.idBtnAddCourseAB);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference  = firebaseDatabase.getReference(Username);
        databaseReferencecm = firebaseDatabase.getReference("Cources");
        courseRVModalArrayList = new ArrayList<>();
        placeOrders= new ArrayList<>();
        bottomSheet = findViewById(R.id.idRLBSheet2);
        mAuth = FirebaseAuth.getInstance();
        courseRVAdapter = new CourseRVAdapter(courseRVModalArrayList,this,this);
        courseRV.setLayoutManager(new LinearLayoutManager(this));
        courseRV.setAdapter(courseRVAdapter);
        getAllCourses();


    }
    private void getAllCourses(){
        placeOrders.clear();
        courseRVModalArrayList.clear();

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.hasChildren()){
                    loadingPB.setVisibility(View.GONE);
                    PlaceOrder pl =snapshot.getValue(PlaceOrder.class);
                    placeOrders.add(pl);
                    //Toast.makeText(MyOrders.this,pl.getCourseName() , Toast.LENGTH_SHORT).show();
                    CourseRVModal cource = map.get(pl.getCourseName());
                    if(cource!=null){
                        courseRVModalArrayList.add(map.get(pl.getCourseName()));
                        courseRVAdapter.notifyDataSetChanged();
                    }else{
                        DatabaseReference deldatabaseReference = firebaseDatabase.getReference(Username).child(pl.getCourseName());
                        deldatabaseReference.removeValue();
                    }

                }
                else{
                    Intent i = new Intent(MyOrders.this,MainActivity.class);
                    i.putExtra("user",Username);
                    startActivity(i);
                    finish();
                }


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
                if(!snapshot.hasChild(Username)) {
                    Intent i = new Intent(MyOrders.this, MainActivity.class);
                    i.putExtra("user", Username);
                    startActivity(i);
                    finish();
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                loadingPB.setVisibility(View.GONE);
                courseRVAdapter.notifyDataSetChanged();
            }

            /**
             * This method will be triggered in the event that this listener either failed at the server, or
             * is removed as a result of the security and Firebase rules. For more information on securing
             * your data, see: <a href="https://firebase.google.com/docs/database/security/quickstart"
             * target="_blank"> Security Quickstart</a>
             *
             * @param error A description of the error that occurred
             */
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MyOrders.this, "Empty Data", Toast.LENGTH_SHORT).show();
            }

        });

    }

    @Override
    public void onCourceClick(int position) {
        //Toast.makeText(MyOrders.this, Integer.toString(position), Toast.LENGTH_SHORT).show();
        displayBottomSheet(courseRVModalArrayList.get(position));
    }

    private void displayBottomSheet(CourseRVModal courseRVModal) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View layout = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_dialog_orders,bottomSheet);
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
        Button CancelBtn = layout.findViewById(R.id.idViewDetails);

        courseNameTV.setText(courseRVModal.getCourseName());
        coursePrice.setText("Rs. "+courseRVModal.getCoursePrice());
        courseDescTV.setText(courseRVModal.getCourseDescription());
        courseSuitedForTV.setText(courseRVModal.getCourseSuitedFor());
        Picasso.get().load(courseRVModal.getCourseImg()).into(courseIV);

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MyOrders.this,ViewProduct.class);
                i.putExtra("course",courseRVModal);
                i.putExtra("user",Username);
                startActivity(i);
            }
        });

        CancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference ndbrdatabaseReference = firebaseDatabase.getReference(Username).child(courseRVModal.getCourseName());
                ndbrdatabaseReference.removeValue();
                Toast.makeText(MyOrders.this, "Course Deleted Successfully...", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(getIntent());
            }
        });
    }
}