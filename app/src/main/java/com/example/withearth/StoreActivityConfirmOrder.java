package com.example.withearth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;


public class StoreActivityConfirmOrder extends AppCompatActivity {

    private EditText nameEditText, phoneEditText, addressEditText;
    private Button confirmOrderBtn;
    private FirebaseAuth auth;
    private String point;
    private int orderNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_store_confirm_order);

        //currentTime = getIntent().getStringExtra("current time");

        confirmOrderBtn = (Button) findViewById(R.id.confirm_final_order_btn);
        nameEditText = (EditText) findViewById(R.id.shippment_name);
        phoneEditText = (EditText) findViewById(R.id.shippment_phone_number);
        addressEditText = (EditText) findViewById(R.id.shippment_address);

        // 사용자 ordernum 가져오기
        DatabaseReference numListRef = FirebaseDatabase.getInstance().getReference();
        numListRef.child("Orders").child(auth.getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener(){
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        if (snapshot.hasChild("ordernum")){
                            int value = snapshot.child("ordernum").getValue(int.class);
                            orderNum = value;

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });

        confirmOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Check();

                /*
                DatabaseReference totalRef = FirebaseDatabase.getInstance().getReference("Orders");
                totalRef = totalRef.child(auth.getCurrentUser().getUid()).child("ordernum");
                totalRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String dpoint = snapshot.getValue(String.class);
                        int totalint = Integer.parseInt(dpoint);
                        double finalpoint = Math.floor(totalint * 0.05);
                        point = String.valueOf(finalpoint);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { }
                });

                DatabaseReference pointRef = FirebaseDatabase.getInstance().getReference("Point")
                        .child(auth.getCurrentUser().getUid());

                HashMap<String, Object> pointMap = new HashMap<>();
                pointMap.put("point", point);
                pointRef.updateChildren(pointMap);

                 */

            }
        });

    }

    //배송지 정보 입력, 빈칸일 경우 toast 출력
    private void Check() {
        if(TextUtils.isEmpty(nameEditText.getText().toString())){
            Toast.makeText(this, "이름을 입력하세요.", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(phoneEditText.getText().toString())){
            Toast.makeText(this, "전화번호를 입력하세요.", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(addressEditText.getText().toString())){
            Toast.makeText(this, "주소를 입력하세요.", Toast.LENGTH_SHORT).show();
        }
        else
            ConfirmOrder();


    }

    //주문 정보 저장 realtime database 이용, Orders 밑에 회원 ID로 저장
    private void ConfirmOrder() {

        DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders")
                .child(auth.getCurrentUser().getUid()).child(String.valueOf(orderNum));
        HashMap<String, Object> ordersMap = new HashMap<>();
        ordersMap.put("name", nameEditText.getText().toString());
        ordersMap.put("phone", phoneEditText.getText().toString());
        ordersMap.put("address", addressEditText.getText().toString());

        ordersRef.updateChildren(ordersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                if (task.isSuccessful()){

                    Intent intent = new Intent(StoreActivityConfirmOrder.this, StoreActivityOrderSuccess.class);
                    intent.putExtra("ordernum", orderNum);
                    startActivity(intent);
                    finish();


                }
            }
        });


    }
}