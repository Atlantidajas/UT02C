package com.practices.jorge.ut02c.Models.UpdateUserInfo;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;

import com.practices.jorge.ut02c.Controllers.DatabaseQueryClass;
import com.practices.jorge.ut02c.Models.CreateUser.User;
import com.practices.jorge.ut02c.R;
import com.practices.jorge.ut02c.Util.Config;


public class UserUpdateDialogFragment extends DialogFragment {

    private static long userRegNo;
    private static int userItemPosition;
    private static UserUpdateListener userUpdateListener;

    private User mUser;

    private EditText nameEditText;
    private EditText phoneEditText;
    private EditText emailEditText;
    private Button updateButton;
    private Button cancelButton;

    private String nameString = "";
    private long registrationNumber = -1;
    private String phoneString = "";
    private String emailString = "";

    private DatabaseQueryClass databaseQueryClass;

    public UserUpdateDialogFragment() {
        // Required empty public constructor
    }

    public static UserUpdateDialogFragment newInstance(long registrationNumber, int position, UserUpdateListener listener){
        userRegNo = registrationNumber;
        userItemPosition = position;
        userUpdateListener = listener;
        UserUpdateDialogFragment userUpdateDialogFragment = new UserUpdateDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", "Modificar datos de usuario");
        userUpdateDialogFragment.setArguments(args);

        userUpdateDialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);

        return userUpdateDialogFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user_update_dialog, container, false);

        databaseQueryClass = new DatabaseQueryClass(getContext());

        nameEditText = view.findViewById(R.id.studentNameEditText);
        phoneEditText = view.findViewById(R.id.phoneEditText);
        emailEditText = view.findViewById(R.id.emailEditText);
        updateButton = view.findViewById(R.id.updateStudentInfoButton);
        cancelButton = view.findViewById(R.id.cancelButton);

        String title = getArguments().getString(Config.TITLE);
        getDialog().setTitle(title);

        mUser = databaseQueryClass.getStudentByRegNum(userRegNo);

        if(mUser !=null){
            nameEditText.setText(mUser.getName());
            phoneEditText.setText(mUser.getPhoneNumber());
            emailEditText.setText(mUser.getEmail());

            updateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    nameString = nameEditText.getText().toString();
                    phoneString = phoneEditText.getText().toString();
                    emailString = emailEditText.getText().toString();

                    mUser.setName(nameString);
                    mUser.setPhoneNumber(phoneString);
                    mUser.setEmail(emailString);

                    long id = databaseQueryClass.updateStudentInfo(mUser);

                    if(id>0){
                        userUpdateListener.onUserInfoUpdated(mUser, userItemPosition);
                        getDialog().dismiss();
                    }
                }
            });

            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getDialog().dismiss();
                }
            });

        }

        return view;
    }

    @Override
    public void onStart() {

        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {

            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;

            dialog.getWindow().setLayout(width, height);
        }
    }

}
