package dev._2lstudios.elasticbungee.utils;

import java.util.Random;

public class StringUtils {

    private final static int LEFT_LIMIT = 97;
    private final static int RIGHT_LIMIT = 122;

    public static String randomString(final int targetStringLength) {
        final Random random = new Random();
        final StringBuilder buffer = new StringBuilder(targetStringLength);

        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = LEFT_LIMIT + (int) (random.nextFloat() * (RIGHT_LIMIT - LEFT_LIMIT + 1));
            buffer.append((char) randomLimitedInt);
        }

        return buffer.toString();
    }
}
