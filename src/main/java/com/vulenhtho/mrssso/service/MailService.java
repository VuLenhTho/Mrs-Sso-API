package com.vulenhtho.mrssso.service;

import com.vulenhtho.mrssso.dto.UserDTO;
import com.vulenhtho.mrssso.entity.User;

public interface MailService {
    void sendActivationEmail(UserDTO userDTO, String activationKey);

    boolean sendPasswordResetMail(User user);
}
