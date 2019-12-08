package com.practices.jorge.ut02c.Models.ShowUserList;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.practices.jorge.ut02c.Controllers.DatabaseQueryClass;
import com.practices.jorge.ut02c.Models.CreateUser.User;
import com.practices.jorge.ut02c.Models.CreateUser.UserCreateDialogFragment;
import com.practices.jorge.ut02c.Models.CreateUser.UserCreateListener;
import com.practices.jorge.ut02c.R;
import com.practices.jorge.ut02c.Util.Config;

import java.util.ArrayList;
import java.util.List;

public class UserListActivity extends AppCompatActivity implements UserCreateListener {

    private DatabaseQueryClass databaseQueryClass = new DatabaseQueryClass(this);

    private List<User> userList = new ArrayList<>();

    private TextView studentListEmptyTextView;
    private RecyclerView recyclerView;
    private UserListRecyclerViewAdapter userListRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_users_list);
        Toolbar toolbar = findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );
        Logger.addLogAdapter( new AndroidLogAdapter() );

        recyclerView = findViewById(R.id.studentRecyclerView);
        studentListEmptyTextView = findViewById(R.id.emptyStudentListTextView);

        userList.addAll(databaseQueryClass.getAllStudent());

        userListRecyclerViewAdapter = new UserListRecyclerViewAdapter(this, userList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(userListRecyclerViewAdapter);

        viewVisibility();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openStudentCreateDialog();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item ) {

        if( item.getItemId() == R.id.action_delete ){

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("Eliminar todos los usuarios: ¿Estás seguro?");
            alertDialogBuilder.setPositiveButton("Aceptar",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            boolean isAllDeleted = databaseQueryClass.deleteAllStudents();
                            if(isAllDeleted){
                                userList.clear();
                                userListRecyclerViewAdapter.notifyDataSetChanged();
                                viewVisibility();
                            }
                        }
                    });

            alertDialogBuilder.setNegativeButton("Cancelar",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }

        return super.onOptionsItemSelected(item);
    }

    public void viewVisibility() {
        if(userList.isEmpty())
            studentListEmptyTextView.setVisibility(View.VISIBLE);
        else
            studentListEmptyTextView.setVisibility(View.GONE);
    }

    private void openStudentCreateDialog() {
        UserCreateDialogFragment userCreateDialogFragment = UserCreateDialogFragment.newInstance("Crear usuario", this);
        userCreateDialogFragment.show(getSupportFragmentManager(), Config.CREATE_USERS);
    }

    @Override
    public void onUserCreated( User user) {
        userList.add(user);
        userListRecyclerViewAdapter.notifyDataSetChanged();
        viewVisibility();
        Logger.d(user.getName());
    }

}
