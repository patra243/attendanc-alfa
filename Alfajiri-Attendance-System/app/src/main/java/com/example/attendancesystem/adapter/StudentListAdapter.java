package com.example.attendancesystem.adapter;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.attendancesystem.R;
import com.example.attendancesystem.model.Student;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class StudentListAdapter extends RecyclerView.Adapter<StudentListAdapter.StudentListViewHolder> {

    private List<Student> studentList;
    private Context context;

    public StudentListAdapter(Context context,List<Student> studentList) {

        this.context = context;
        this.studentList = studentList;
    }

    public  StudentListAdapter(){

    }

    @NonNull
    @Override
    public StudentListAdapter.StudentListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View view=layoutInflater.inflate(R.layout.single_student_layout,viewGroup,false);
        return new StudentListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final StudentListViewHolder studentListViewHolder, @SuppressLint("RecyclerView") final int position) {
        studentListViewHolder.Student_name.setText(studentList.get(studentListViewHolder.getAdapterPosition()).getName());
        studentListViewHolder.course_code.setText(studentList.get(studentListViewHolder.getAdapterPosition()).getCourse_code());
        studentListViewHolder.id.setText(studentList.get(studentListViewHolder.getAdapterPosition()).getId());

        String name = studentList.get(position).getName();
        final String digit = studentList.get(position).getId();
        ((StudentListViewHolder) studentListViewHolder).Student_name.setText(name);

        studentListViewHolder.Student_name.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                CharSequence[] option = new CharSequence[]{
                        "Delete",
                        "Cancel",
                };
                    AlertDialog.Builder builder = new AlertDialog.Builder(studentListViewHolder.itemView.getContext());
                    builder.setTitle("Delete Content");
                builder.setItems(option, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0){
                            delete(position, digit);
                        }
                    }
                });

                builder.create().show();
                return false;
            }
        });
    }

    private void delete(int position, String digit) {

        DatabaseReference delRef = FirebaseDatabase.getInstance().getReference().child("Department").child("Student").child("allstudent");
        Query query = delRef.child(digit);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                snapshot.getRef().removeValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    class StudentListViewHolder extends  RecyclerView.ViewHolder {
        TextView Student_name;
        TextView course_code;
        TextView id;
        TextView txt_option;
        public StudentListViewHolder(@NonNull View itemView) {
            super(itemView);
            Student_name = itemView.findViewById(R.id.studentNameTV);
            course_code = itemView.findViewById(R.id.studentCourseTv);
            id = itemView.findViewById(R.id.studentIDv);
            txt_option = itemView.findViewById(R.id.txt_option);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateCollection(List<Student> studentList){
        this.studentList=studentList;
        notifyDataSetChanged();
    }
}
