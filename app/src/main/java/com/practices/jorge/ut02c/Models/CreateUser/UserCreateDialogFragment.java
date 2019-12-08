package com.practices.jorge.ut02c.Models.CreateUser;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;

import com.practices.jorge.ut02c.Controllers.DatabaseQueryClass;
import com.practices.jorge.ut02c.R;
import com.practices.jorge.ut02c.Util.Config;


public class UserCreateDialogFragment extends DialogFragment {

    private static UserCreateListener userCreateListener;

    private EditText nameEditText;
    private EditText phoneEditText;
    private EditText emailEditText;
    private Button createButton;
    private Button cancelButton;

    private String nameString = "";
    private String phoneString = "";
    private String emailString = "";

    public UserCreateDialogFragment() {}

    public static UserCreateDialogFragment newInstance(String title, UserCreateListener listener){
        userCreateListener = listener;
        UserCreateDialogFragment userCreateDialogFragment = new UserCreateDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        userCreateDialogFragment.setArguments(args);

        userCreateDialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);

        return userCreateDialogFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState ) {

        View view = inflater.inflate( R.layout.fragment_user_create_dialog, container, false );

        nameEditText = view.findViewById(R.id.studentNameEditText);
        phoneEditText = view.findViewById(R.id.phoneEditText);
        emailEditText = view.findViewById(R.id.emailEditText);
        createButton = view.findViewById(R.id.createButton);
        cancelButton = view.findViewById(R.id.cancelButton);

        String title = getArguments().getString( Config.TITLE );
        getDialog().setTitle(title);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameString = nameEditText.getText().toString();
                phoneString = phoneEditText.getText().toString();
                emailString = emailEditText.getText().toString();

                User user = new User(-1, nameString, phoneString, emailString);

                DatabaseQueryClass databaseQueryClass = new DatabaseQueryClass(getContext());

                long id = databaseQueryClass.insertStudent(user);

                if(id>0){
                    user.setId(id);
                    userCreateListener.onUserCreated(user);
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
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if ( dialog != null ) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setLayout( width, height );
        }
    }

}
