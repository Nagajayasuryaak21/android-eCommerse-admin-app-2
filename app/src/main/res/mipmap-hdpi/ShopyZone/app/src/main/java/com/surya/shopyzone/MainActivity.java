package com.surya.shopyzone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
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
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements CourseRVAdapter.CourseClickInterface {

    private RecyclerView courseRV;
    private ProgressBar loadingPB;
    private FloatingActionButton addFAB;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference,databaseReferencePlaceOrder;
    private RelativeLayout bottomSheet;
    private ArrayList<CourseRVModal> courseRVModalArrayList;
    private CourseRVAdapter courseRVAdapter;
    private FirebaseAuth mAuth;
    private HashMap<String,CourseRVModal> map;
    private String userName="Cources";
    private int k=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        courseRV = findViewById(R.id.idRVCourses);
        userName = getIntent().getStringExtra("user");
        loadingPB = findViewById(R.id.idPBLoading);
        addFAB = findViewById(R.id.idBtnAddCourseAB);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference  = firebaseDatabase.getReference("Courses");
        courseRVModalArrayList = new ArrayList<>();
        bottomSheet = findViewById(R.id.idRLBSheet);
        mAuth = FirebaseAuth.getInstance();
        map=new HashMap<>();
        courseRVAdapter = new CourseRVAdapter(courseRVModalArrayList,this,this);
        courseRV.setLayoutManager(new LinearLayoutManager(this));
        courseRV.setAdapter(courseRVAdapter);
        databaseReferencePlaceOrder=firebaseDatabase.getReference(userName);

        addFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MainActivity.this, "Username :"+userName, Toast.LENGTH_SHORT).show();
                DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.hasChild(userName)){
                            Intent i= new Intent(MainActivity.this,MyOrders.class);
                            i.putExtra("map", map);
                            i.putExtra("user",userName);
                            startActivity(i);
                        }
                        else{
                            Toast.makeText(MainActivity.this, "No Orders Recordrd", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

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
                CourseRVModal CVM = snapshot.getValue(CourseRVModal.class);
                map.put(CVM.getCourseName(),CVM);
                courseRVModalArrayList.add(CVM);
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
        Button viewDetailsBtn = layout.findViewById(R.id.idPlaceOrder);

        courseNameTV.setText(courseRVModal.getCourseName());
        coursePrice.setText("Rs. "+courseRVModal.getCoursePrice());
        courseDescTV.setText(courseRVModal.getCourseDescription());
        courseSuitedForTV.setText(courseRVModal.getCourseSuitedFor());
        Picasso.get().load(courseRVModal.getCourseImg()).into(courseIV);

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,ViewProduct.class);
                i.putExtra("course",courseRVModal);
                i.putExtra("user",userName);
                startActivity(i);
            }
        });

        viewDetailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  loadingPB.setVisibility(View.VISIBLE);
//                Intent i = new Intent(Intent.ACTION_VIEW);
//                i.setData(Uri.parse(courseRVModal.getCourseLink()));
//                startActivity(i);
                  k=1;
                  //Toast.makeText(MainActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
                  PlaceOrder newOrder = new PlaceOrder(courseRVModal.getCourseName());
                  databaseReferencePlaceOrder.addValueEventListener(new ValueEventListener() {
                      @Override
                      public void onDataChange(@NonNull DataSnapshot snapshot) {
                          if(k==1) {
                              databaseReferencePlaceOrder.child(courseRVModal.getCourseName()).setValue(newOrder);
                              loadingPB.setVisibility(View.GONE);
                              Toast.makeText(MainActivity.this, "Placed Order successfully...!", Toast.LENGTH_SHORT).show();
                              k=0;
                          }

                      }

                      @Override
                      public void onCancelled(@NonNull DatabaseError error) {
                          loadingPB.setVisibility(View.GONE);
                          Toast.makeText(MainActivity.this, "Error in Placing Order...", Toast.LENGTH_SHORT).show();

                      }
                  });
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