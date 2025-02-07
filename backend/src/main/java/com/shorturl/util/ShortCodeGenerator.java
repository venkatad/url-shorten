package com.shorturl.util;

import org.springframework.stereotype.Service;

@Service
public class ShortCodeGenerator {

    // Map to store 62 possible characters
    protected static char map[] = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();

    /**
     * Converts an integer into a base-62 encoded short code.
     *
     * @param n The integer to be converted.
     * @return The base-62 encoded short code.
     */
    protected static String intToShortCode(int n) {

        StringBuilder shorturl = new StringBuilder();
        // Convert given integer id to a base 62 number
        while (n > 0) {
            // use above map to store actual character
            // in short url
            shorturl.append(map[n % 62]);
            n = n / 62;
        }
        // Reverse shortURL to complete base conversion
        return shorturl.reverse().toString();
    }

    /**
     * Generates a purely random short code of 8 characters.
     *
     * @return A randomly generated 8-character short code.
     */
    public String pureRandom() {
        String code = "";
        int len = 8;
        int max = map.length - 1;
        int min = 0;
        int range = max - min + 1;
        // Generate an 8-character random string from the character map
        for (int a = 1; a <= len; a++) {
            // Generate a random index
            int rand = (int) (Math.random() * range) + min;
            // Append the randomly selected character
            code += map[rand];
        }
        return code;
    }

    /**
     * Generates a short code using a random approach.
     * 
     * @return The generated short code.
     */
    public String generate() {
        // generate a random integer
        // int x = (int)(new Date().getTime()/1000);
        // //use this integer to generate the short code
        // String code = intToShortCode(x);
        String code = pureRandom();
        return code;
    }
}
