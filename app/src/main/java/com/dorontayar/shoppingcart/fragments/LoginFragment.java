package com.dorontayar.shoppingcart.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

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


    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
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
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        Button buttonRegister = view.findViewById(R.id.buttonRegister);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_registerFragment);
            }
        });

        Button buttonLogin = view.findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userName = ((EditText)view.findViewById(R.id.editTextUserName)).getText().toString().trim();
                String password = ((EditText)view.findViewById(R.id.editTextPassword)).getText().toString();
                if (!userName.isEmpty() && !password.isEmpty()){
                    Log.w("Login","Attempting to Log in");
                    loginFunc(userName,password,v);
                } else{
                    Toast.makeText(getActivity(), "Email or Password is empty", Toast.LENGTH_LONG).show();
                }

            }
        });

        return view;
    }
    public void loginFunc(String userName,String password, View v){

        // Checking if the User already exists, if not then register it in the database
        databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener(){

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // checks if the user name exist in the database
                if(snapshot.hasChild(userName)){

                    // the user exist in the database
                    // checks if the password are correct

                    final String getUserPassword = snapshot.child(userName).child("password").getValue(String.class);
                    if(getUserPassword.equals(password)){
                        Toast.makeText(getActivity(), "Login in", Toast.LENGTH_LONG).show();
                        Bundle bundle = new Bundle();
                        bundle.putString("user_name",userName);
                        Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_mainScreenFragment,bundle);
                        Toast.makeText(getActivity(), "Welcome "+userName, Toast.LENGTH_LONG).show();


                    }
                    else {
                        Toast.makeText(getActivity(), "Wrong password", Toast.LENGTH_LONG).show();
                    }


                }
                else {
                    Toast.makeText(getActivity(),"User does not exists, please register!",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}