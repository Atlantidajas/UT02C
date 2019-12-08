package com.practices.jorge.ut02c.Models.UpdateUserInfo;

import com.practices.jorge.ut02c.Models.CreateUser.User;

public interface UserUpdateListener {
    void onUserInfoUpdated(User user, int position);
}
