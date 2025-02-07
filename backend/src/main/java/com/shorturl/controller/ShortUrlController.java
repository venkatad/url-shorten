package com.shorturl.controller;

import com.shorturl.entity.ApiResponse;
import com.shorturl.entity.PagedUrls;
import com.shorturl.entity.ShortUrl;
import com.shorturl.service.LoggerService;
import com.shorturl.util.ShortCodeGenerator;
import com.shorturl.service.ShortUrlService;
import com.shorturl.service.UrlValidatorService;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ShortUrlController {

    @Autowired
    private UrlValidatorService urlValidatorService;
    @Autowired
    private ShortUrlService shortUrlService;
    @Autowired
    private ShortCodeGenerator shortCodeGenerator;
    @Autowired
    private LoggerService loggerService;

    /**
     * Endpoint to create a short URL.
     * 
     * @param url The URL to be shortened (provided as a request parameter)
     * @return ApiResponse containing the shortened URL or an error message
     */
    @GetMapping("/create")
    public ApiResponse createShortUrl(@RequestParam(value = "url", defaultValue = "") String url) {
        loggerService.debug("/create called with url: " + url);
        // Check if the URL is empty
        if ("".equals(url)) {
            loggerService.error("Url is empty");
            return ApiResponse.create(400, "Url required", null);
        }
        try {
            // Validate the given URL
            urlValidatorService.validate(url);
        } catch (Exception ex) {
            loggerService.error("Error in url validation: " + ex.getMessage());
            return ApiResponse.create(500, ex.getMessage(), null);
        }
        // check if the url already exists in database
        Optional<ShortUrl> existingSurl = shortUrlService.getByFullUrl(url);
        if (!existingSurl.isEmpty()) {
            loggerService.debug("Url already exists. Returning the same record");
            return ApiResponse.create(200, null, existingSurl.get().getShortUrl());
        }
        // generate the short code
        String code = shortCodeGenerator.generate();
        while (true) {
            // if short code already exists
            if (shortUrlService.shortCodeExists(code)) {
                // generate again
                code = shortCodeGenerator.generate();
            }
            break;
        }
        loggerService.debug("Code generated: " + code);
        String shortUrl = "http://localhost:8080/u/" + code;
        ShortUrl surl = new ShortUrl();
        surl.setFullUrl(url);
        surl.setShortCode(code);
        surl.setShortUrl(shortUrl);
        // save the data in database
        surl = shortUrlService.add(surl);
        return ApiResponse.create(200, null, shortUrl);
    }

    /**
     * Endpoint to retrieve the original long URL from a short code.
     * 
     * @param code The short code associated with the URL
     * @return ApiResponse containing the full URL or an error message if not found
     */

    @GetMapping("/u/{code}")
    public ApiResponse getByShortCode(@PathVariable String code) {
        loggerService.debug("/u/{code} called with the code: " + code);
        Optional<ShortUrl> surl = shortUrlService.getByShortCode(code);
        if (surl.isEmpty()) {
            loggerService.error("URL not in database");
            return ApiResponse.create(404, "URL not found in database", null);
        }
        return ApiResponse.create(200, null, surl.get().getFullUrl());
    }

    /**
     * Endpoint to list all short URLs with pagination.
     * 
     * @param page The requested page number (default is 1)
     * @return ApiResponse containing a paginated list of short URLs
     */
    @GetMapping("/all")
    public ApiResponse listAll(@RequestParam(value = "page", defaultValue = "1") String page) {
        loggerService.debug("/all called");
        List<ShortUrl> urls = shortUrlService.getAllShortUrls();
        urls.sort(new ShortUrlIdComparator());
        int recordsPerPage = 5;
        int currentPage = Integer.parseInt(page);
        int totalPages = (int) Math.ceil(urls.size() / (double) recordsPerPage);
        int startIndex = (currentPage - 1) * recordsPerPage;
        int endIndex = startIndex + recordsPerPage;
        if (endIndex > urls.size()) {
            endIndex = urls.size();
        }
        PagedUrls pu = new PagedUrls();
        pu.page = currentPage;
        pu.recordsPerPage = recordsPerPage;
        pu.totalPages = totalPages;
        if (startIndex < (urls.size())) {
            pu.urls = urls.subList(startIndex, endIndex);
        }
        return ApiResponse.create(200, null, pu);
    }

    /**
     * Comparator class to sort short URLs by ID in descending order.
     */
    class ShortUrlIdComparator implements Comparator<ShortUrl> {
        @Override
        public int compare(ShortUrl a, ShortUrl b) {
            return b.getId().compareTo(a.getId());
        }
    }
}
