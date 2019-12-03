package br.com.devdojo;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEnconder {

    public static void main(String[] args) {
        BCryptPasswordEncoder pwd = new BCryptPasswordEncoder();
        System.out.println(pwd.encode("1234"));
    }
}
