package com.dorontayar.shoppingcart.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dorontayar.shoppingcart.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    // Firebase related parameters
    private FirebaseAuth mAuth;

    // Creating FireBase DatabaseReference to access firebase realtime database
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://shoppingcart-e87c8-default-rtdb.firebaseio.com/");

    public RegisterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_register, container, false);

        Button buttonRegister = view.findViewById(R.id.buttonRegisterInRegister);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String userName = ((EditText)view.findViewById(R.id.editTextUserName)).getText().toString().trim();
                String password = ((EditText)view.findViewById(R.id.editTextPasswordInRegister)).getText().toString();
                String conPassword = ((EditText)view.findViewById(R.id.editTextConPasswordInRegister)).getText().toString();
                String phoneNumber = ((EditText)view.findViewById(R.id.editTextPhoneNumber)).getText().toString();


                if (userName.isEmpty() || password.isEmpty() || phoneNumber.isEmpty() || conPassword.isEmpty()){
                    Toast.makeText(getActivity(), "Please fill all fields", Toast.LENGTH_LONG).show();
                }
                else if(!password.equals(conPassword)){
                    Toast.makeText(getActivity(), "Passwords do not match", Toast.LENGTH_LONG).show();
                }
                else{
                    Log.w("Register","Attempting to Register");
                    regFunc(userName,password,phoneNumber,v);
                }
            }
        });

        return view;
    }
    public void regFunc(String userName, String password, String phoneNumber, View v){

       if (password.length() < 6) {
            Toast.makeText(getActivity(), "Password is too short ! should be longer than 6 characters", Toast.LENGTH_SHORT).show();
        } else{
           // Checking if the User already exists, if not then register it in the database
           databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener(){

               @Override
               public void onDataChange(@NonNull DataSnapshot snapshot) {
                   if(snapshot.hasChild(userName)){
                       Toast.makeText(getActivity(),"User name already exists!",Toast.LENGTH_SHORT).show();
                   }
                   else {
                       databaseReference.child("users").child(userName).child("password").setValue(password);
                       databaseReference.child("users").child(userName).child("phone_number").setValue(phoneNumber);

                       Toast.makeText(getActivity(), "Register successful!", Toast.LENGTH_LONG).show();

                       Bundle bundle = new Bundle();
                       bundle.putString("user_name",userName);
                       Navigation.findNavController(v).navigate(R.id.action_registerFragment_to_mainScreenFragment,bundle);
                   }
               }

               @Override
               public void onCancelled(@NonNull DatabaseError error) {

               }
           });


        }

    }
}