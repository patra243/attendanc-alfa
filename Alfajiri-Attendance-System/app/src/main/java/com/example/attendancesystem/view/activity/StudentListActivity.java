package com.example.attendancesystem.view.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import androidx.annotation.NonNull;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.attendancesystem.R;
import com.example.attendancesystem.adapter.StudentListAdapter;
import com.example.attendancesystem.model.Student;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class StudentListActivity extends AppCompatActivity {
   private Toolbar studentListToolbar;
   private FloatingActionButton addStudentBtn;
   private DatabaseReference studentListRef;

   private List<Student> studentList=new ArrayList<>();
   StudentListAdapter studentListAdapter;

   private RecyclerView studentListRV;
   private String intentDept;
   private String intentBatch;
   private String intentShift;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);

        studentListToolbar=findViewById(R.id.studentListToolbar);
        addStudentBtn=findViewById(R.id.addStudentBtn);
        studentListRV=findViewById(R.id.StudentListRV);
        Intent intent=getIntent();
        intentDept= intent.getStringExtra("DEPT");
        intentBatch=intent.getStringExtra("BATCH");
        intentShift=intent.getStringExtra("SHIFT");

        setSupportActionBar(studentListToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(StudentListActivity.this);

        linearLayoutManager.setReverseLayout(true);
        studentListRV.setHasFixedSize(true);
        studentListRV.setLayoutManager(linearLayoutManager);
        studentList = new ArrayList<>();


        studentListRef=FirebaseDatabase.getInstance().getReference().child("Department").child(intentDept).child("Student").child(intentBatch).child("allstudent").child(intentShift);
        studentListRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                studentList.clear();
                if(dataSnapshot.exists()) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        if (dataSnapshot1.hasChildren()) {
                            Student student = dataSnapshot1.getValue(Student.class);
                            studentList.add(student);
                            studentListAdapter = new StudentListAdapter(StudentListActivity.this, studentList);
                        }

                        StudentListAdapter studentListAdapter=new StudentListAdapter(StudentListActivity.this,studentList);
                        studentListRV.setLayoutManager(new LinearLayoutManager(StudentListActivity.this));
                        studentListAdapter.notifyDataSetChanged();
                        studentListRV.setAdapter(studentListAdapter);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        /*studentListRV.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });*/

        addStudentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(StudentListActivity.this,AddStudentActivity.class);

                intent1.putExtra("DEPT",intentDept);
                intent1.putExtra("BATCH",intentBatch);
                intent1.putExtra("SHIFT",intentShift);

                startActivity(intent1);
            }
        });
    }

    /*private void showUpdateDialog(String name, String ID){

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText textViewName = (EditText) dialogView.findViewById(R.id.editStudentName);
        final EditText textViewID = (EditText) dialogView.findViewById(R.id.editStudentID);
        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.updateBtn);

        dialogBuilder.setTitle("Updating User "+ID);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==android.R.id.home){
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
