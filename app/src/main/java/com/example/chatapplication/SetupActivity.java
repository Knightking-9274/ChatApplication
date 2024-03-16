package com.example.chatapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.chatapplication.databinding.ActivitySetupBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.URI;

public class SetupActivity extends AppCompatActivity {
    ActivitySetupBinding bindingSetup;
    FirebaseAuth auth;
    FirebaseDatabase databse;

    FirebaseStorage storage;

    Uri imageProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        bindingSetup = ActivitySetupBinding.inflate(getLayoutInflater());
        setContentView(bindingSetup.getRoot());
        auth = FirebaseAuth.getInstance();
        databse = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        bindingSetup.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setAction(Intent.ACTION_GET_CONTENT);
                i.setType("image/*");
                startActivityForResult(i,45);
            }
        });

        bindingSetup.btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = bindingSetup.txtName.getText().toString();
                if(userName.isEmpty()){
                    bindingSetup.txtName.setError("Please Enter a Name!");
                    return;
                }
                if(imageProfile !=null){
                    StorageReference ref = storage.getReference().child("Profiles").child(auth.getUid());
                    ref.putFile(imageProfile).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if(task.isSuccessful()){
                                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String uriUser = uri.toString();
                                        String uID = auth.getUid();
                                        String phone = auth.getCurrentUser().getPhoneNumber();
                                        String name = bindingSetup.txtName.getText().toString();
                                        User user = new User(uID,name,phone,uriUser);
                                        databse.getReference().child("Users").child(uID).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Intent i = new Intent(SetupActivity.this,MainActivity.class);
                                                startActivity(i);
                                                finish();
                                            }
                                        });
                                    }
                                });
                            }
                        }
                    });
                }else{

                    String uID = auth.getUid();
                    String phone = auth.getCurrentUser().getPhoneNumber();
                    String name = bindingSetup.txtName.getText().toString();
                    User user = new User(uID,name,phone,"No Image");
                    databse.getReference().child("Users").child(uID).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Intent i = new Intent(SetupActivity.this,MainActivity.class);
                            startActivity(i);
                            finish();
                        }
                    });
                }
            }
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null){
            if(data.getData() !=null){
                bindingSetup.imageView.setImageURI(data.getData());
                imageProfile = data.getData();
            }
        }
    }
}