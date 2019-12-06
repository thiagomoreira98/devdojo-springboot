package br.com.devdojo.config;

public class SecurityConstants {
    // Authorization Bearer "hash"
    static final String SECRET = "DevDojoEssentials";
    static final String TOKEN_PREFIX = "Bearer ";
    static final String HEADER_STRING = "Authorization";
    static final String SIGN_UP_URL = "/users/sign-up";
    static final long EXPIRATION_TIME = 86400000L;

}
