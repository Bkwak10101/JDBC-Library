package com.github.bkwak.library;

import org.apache.commons.codec.digest.DigestUtils;
import com.github.bkwak.library.core.Authenticator;

import java.time.LocalDateTime;

public class App2 {
    public static void main(String[] args) {
        String hash = DigestUtils.md5Hex("admin" + Authenticator.seed);
        System.out.println(hash);

    }
}
