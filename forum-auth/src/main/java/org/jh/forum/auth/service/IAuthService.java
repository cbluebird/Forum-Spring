package org.jh.forum.auth.service;

import org.jh.forum.auth.dto.UserDTO;

public interface IAuthService {
    void login(UserDTO userDTO);
}