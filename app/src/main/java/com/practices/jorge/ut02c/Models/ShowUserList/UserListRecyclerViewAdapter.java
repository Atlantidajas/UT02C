package com.practices.jorge.ut02c.Models.ShowUserList;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.practices.jorge.ut02c.Controllers.DatabaseQueryClass;
import com.practices.jorge.ut02c.Models.CreateUser.User;
import com.practices.jorge.ut02c.Models.UpdateUserInfo.UserUpdateDialogFragment;
import com.practices.jorge.ut02c.Models.UpdateUserInfo.UserUpdateListener;
import com.practices.jorge.ut02c.R;
import com.practices.jorge.ut02c.Util.Config;

import java.util.List;

public class UserListRecyclerViewAdapter extends RecyclerView.Adapter<CustomViewHolder> {

    private Context context;
    private List<User> userList;
    private DatabaseQueryClass databaseQueryClass;

    public UserListRecyclerViewAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
        databaseQueryClass = new DatabaseQueryClass(context);
        Logger.addLogAdapter(new AndroidLogAdapter());
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_item, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        final int itemPosition = position;
        final User user = userList.get(position);

        holder.nameTextView.setText(user.getName());
        holder.emailTextView.setText(user.getEmail());
        holder.phoneTextView.setText(user.getPhoneNumber());

        holder.crossButtonImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setMessage("Eliminar usuario. ¿Estás seguro?");
                alertDialogBuilder.setPositiveButton("Aceptar",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                deleteStudent(itemPosition);
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
        });

        holder.editButtonImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserUpdateDialogFragment userUpdateDialogFragment = UserUpdateDialogFragment.newInstance(user.getId(), itemPosition, new UserUpdateListener() {
                    @Override
                    public void onUserInfoUpdated(User student, int position) {
                        userList.set(position, student);
                        notifyDataSetChanged();
                    }
                });
                userUpdateDialogFragment.show(((UserListActivity) context).getSupportFragmentManager(), Config.UPDATE_USERS);
            }
        });
    }

    private void deleteStudent(int position) {
        User user = userList.get(position);
        long count = databaseQueryClass.deleteStudentByRegNum(user.getId());

        if(count>0){
            userList.remove(position);
            notifyDataSetChanged();
            ((UserListActivity) context).viewVisibility();
            Toast.makeText(context, "Usuario eliminado.", Toast.LENGTH_LONG).show();
        } else
            Toast.makeText(context, "No se ha podido eliminar usuario.", Toast.LENGTH_LONG).show();

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
}
