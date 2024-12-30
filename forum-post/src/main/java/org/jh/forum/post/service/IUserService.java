package org.jh.forum.post.service;

import org.jh.forum.post.dto.UserDTO;

public interface IUserService {
    UserDTO ensureUserExist(String userId);

    void checkAdminPermission(String userId);
}