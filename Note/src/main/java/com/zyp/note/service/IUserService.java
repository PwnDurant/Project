package com.zyp.note.service;

import com.zyp.note.common.base.R;
import com.zyp.note.pojo.domain.UserInfo;

public interface IUserService {
    R<Boolean> login(UserInfo userInfo);

    Boolean register(UserInfo userInfo);
}
