package com.bitc502.grapemarket.singleton;

import com.bitc502.grapemarket.model.CurrentUserInfo;

public class Session {
    public static CurrentUserInfo currentUserInfo = CurrentUserInfo.getInstance();
}
