package com.vulenhtho.mrssso.config;

import java.net.URI;

public class Constant {
    public static final String MAIL_SIGNATURE = "<p>Mrs.Sso Shop</p><p>Địa chỉ: 55 đường y, Học viện Nông nghiệp Việt Nam</p><p>Điện thoại: 0353 113 548</p>";
    public static final String MAIL_IMAGE = "<img width=\"400px\" height=\"200px\" src=\"https://dcassetcdn.com/design_img/3677416/735008/735008_21466886_3677416_92a17a16_image.jpg\">";

    //sort
    public static final String DATE_DES = "date-des";
    public static final String DATE_ASC = "date-asc";
    public static final String MODIFIED_DES = "mod-des";
    public static final String MODIFIED_ASC = "mod-asc";

    public static final URI DEFAULT_TYPE = URI.create("https://www.jhipster.tech/problem/problem-with-message");

    public static class USER_ERROR_MESSAGE {
        public static final String USERNAME_EXISTED = "USERNAME_EXISTED";
        public static final String PHONE_EXISTED = "PHONE_EXISTED";
        public static final String EMAIL_EXISTED = "EMAIL_EXISTED";
    }
}
