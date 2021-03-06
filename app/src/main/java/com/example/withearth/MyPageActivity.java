package com.example.withearth;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyPageActivity extends Fragment {
    private View view;
    private DatabaseReference databaseReference;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth mFirebaseAuth;



    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirebaseAuth = FirebaseAuth.getInstance();
        setHasOptionsMenu(true);

    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_mypage, container, false);

        //????????? ?????? ???
        if(mFirebaseAuth.getCurrentUser() != null){

            mFirebaseAuth = FirebaseAuth.getInstance();  //FirebaseAuth ?????? ?????????

            //????????? ??? ????????? ??????
            final FirebaseUser user = mFirebaseAuth.getCurrentUser();
            TextView emailIdData = view.findViewById(R.id.emailIdData);
            emailIdData.setText(user.getEmail());

            //?????? ??????
            TextView nameData = view.findViewById(R.id.nameData);
            databaseReference = database.getReference("Users").child(mFirebaseAuth.getCurrentUser().getUid());
            databaseReference.child("name").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String name = snapshot.getValue(String.class);
                    nameData.setText(name);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) { }
            });

            //????????? ??????
            TextView tv_point = view.findViewById(R.id.tv_point);
            databaseReference = database.getReference("Point").child(mFirebaseAuth.getCurrentUser().getUid());
            databaseReference.child("point").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String point = snapshot.getValue(String.class);
                    tv_point.setText(point+" Point");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


            //???????????? ??????
            Button btn_logout = (Button) view.findViewById(R.id.btn_logout);
            btn_logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //???????????? ??????
                    mFirebaseAuth.signOut();
                    Toast.makeText(v.getContext(), "???????????? ???????????????.", Toast.LENGTH_SHORT).show();

                    //???????????? ?????? LoginActivity??? ??????
                    Intent intent = new Intent(v.getContext(), LoginActivity.class);
                    startActivity(intent);
                }
            });


            //?????? ??????
            Button btn_delete = (Button) view.findViewById(R.id.btn_delete);
            btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //?????? ????????? ????????? ?????? ?????????
                    AlertDialog.Builder ad = new AlertDialog.Builder(v.getContext());
                    ad.setIcon(R.drawable.ic_environment);
                    ad.setTitle("?????? ??????");
                    ad.setMessage("        ?????? ?????? ??? ????????? ????????? ?????????\n         ????????? ???????????? ?????? ?????? ?????? \n            ???????????? ?????? ???????????????.\n\n    ????????? ??????????????? ?????????????????????????");

                    //?????? ??????
                    ad.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //?????? ??????
                            mFirebaseAuth.getCurrentUser().delete();
                            Toast.makeText(v.getContext(), "?????? ????????? ?????????????????????.", Toast.LENGTH_SHORT).show();

                            //??????????????? ????????? ??????
                            dialog.dismiss();

                            //?????? ?????? LoginActivity??? ??????
                            Intent intent = new Intent(v.getContext(), LoginActivity.class);
                            startActivity(intent);
                        }
                    });
                    //?????? ??????
                    ad.setNegativeButton("??????", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //??????????????? ????????? ??????
                            dialog.dismiss();
                        }
                    });
                    ad.show();

                }
            });

            Button btn_myinfo = (Button) view.findViewById(R.id.btn_myinfo);
            btn_myinfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), PwdCheckActivity.class);
                    startActivity(intent);
                }
            });

            //???????????? ?????? ??? ?????? ??????
            Button btn_collecting_using = (Button) view.findViewById(R.id.btn_collecting_using);
            btn_collecting_using.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //MyPageActivityCollectingUsing?????? ??????
                    Intent intent = new Intent(v.getContext(), MyPageActivityCollectingUsing.class);
                    startActivity(intent);
                }
            });


        }
        //????????? ?????? ????????? ???
        else{
            MyPageActivityUnlogin myPageActivityUnlogin = new MyPageActivityUnlogin();
            ((MainActivity)getActivity()).replaceFragment(myPageActivityUnlogin);
        }



        return view;
    }


    //?????? ??????
    @Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_base_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_cart:
                Intent intent = new Intent(getActivity(), StoreActivityCart.class);
                startActivity(intent);
                return true;

            default :
                return super.onOptionsItemSelected(item) ;
        }
    }

}
