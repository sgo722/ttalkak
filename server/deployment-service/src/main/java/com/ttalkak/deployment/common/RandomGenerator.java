package com.ttalkak.deployment.common;

import java.util.Random;
import java.util.UUID;

public class RandomGenerator {

    public static String generateRandomString(int length) {
        String uuid = UUID.randomUUID().toString().replace("-", "").substring(0, length);

        if(Character.isDigit(uuid.charAt(0))){
            Random random = new Random();
            char firstChar = (char)('a' + random.nextInt(26));
            uuid = firstChar + uuid.substring(1);
        }

        return uuid;
    }
}
