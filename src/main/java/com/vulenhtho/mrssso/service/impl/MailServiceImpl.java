package com.vulenhtho.mrssso.service.impl;

import com.vulenhtho.mrssso.config.Constant;
import com.vulenhtho.mrssso.dto.UserDTO;
import com.vulenhtho.mrssso.entity.User;
import com.vulenhtho.mrssso.service.MailService;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class MailServiceImpl implements MailService {
    private final JavaMailSender javaMailSender;

    public MailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendActivationEmail(UserDTO userDTO, String activationKey){
        MimeMessage msg = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(msg, true);

            helper.setTo(userDTO.getEmail());
            helper.setSubject("MÃ KÍCH HOẠT TÀI KHOẢN Mrs. Sso");

            helper.setText("<div style=\"color: brown;\"><h2>Xin chào, </h2><h3>Đây là mã kích hoạt tài khoản Mrs. Sso của bạn: <b>"
                    + activationKey +"</b></h3>" + Constant.MAIL_IMAGE +" </div>" + Constant.MAIL_SIGNATURE, true);

            javaMailSender.send(msg);
        }  catch (MailException | MessagingException e) {
            e.printStackTrace();
        }
    }

    public boolean sendPasswordResetMail(User user){
        if (user == null) return false;
        MimeMessage msg = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(msg, true);

            helper.setTo(user.getEmail());
            helper.setSubject("MÃ RESET MẬT KHẨU Mrs. Sso");

            helper.setText("<div style=\"color: brown;\"><h2>Xin chào, "+ user.getFullName() +"</h2><h3>Đây là mã reset mật khẩu tài khoản Mrs. Sso của bạn: <b>"
                    + user.getResetKey() +"</b></h3>" + Constant.MAIL_IMAGE +" </div>" + Constant.MAIL_SIGNATURE, true);

            javaMailSender.send(msg);
        }  catch (MailException | MessagingException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
