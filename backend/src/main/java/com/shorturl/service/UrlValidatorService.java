package com.shorturl.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import org.springframework.stereotype.Service;

@Service
public class UrlValidatorService {

    /**
     * Validates the given URL by checking its format and attempting a connection.
     * 
     * @param url The URL string to validate.
     * @throws Exception If the URL is invalid or cannot be connected to.
     */
    public void validate(String url) throws Exception {
        // Ensure the URL starts with "http://" or "https://"
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "https://" + url;
        }
        URL u;
        try {
            // Attempt to create a valid URL object and convert it to a URI
            u = new URL(url);
            u.toURI();
        } catch (MalformedURLException | URISyntaxException e) {
            throw new Exception("Invalid URL");
        }
        try {
            // Open a connection to the URL
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
            InputStream is = conn.getInputStream();
            // Close the input stream after reading
            is.close();
            // Disconnect the connection to free up resources
            conn.disconnect();
        } catch (IOException ex) {
            throw new Exception("Cannot connect to the URL");
        }
    }
}
