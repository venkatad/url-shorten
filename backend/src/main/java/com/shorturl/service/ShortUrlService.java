package com.shorturl.service;

import com.shorturl.entity.ShortUrl;
import com.shorturl.repository.ShortUrlRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShortUrlService {

    @Autowired
    private ShortUrlRepository shortUrlRepository;

    /**
     * Checks if a short code already exists in the database.
     * 
     * @param shortCode The short code to check.
     * @return true if the short code exists, false otherwise.
     */
    public boolean shortCodeExists(String shortCode) {
        Optional<ShortUrl> surl = shortUrlRepository.findByShortCode(shortCode);
        return !surl.isEmpty();
    }

    /**
     * Checks if a short URL already exists in the database.
     * 
     * @param shortUrl The short URL to check.
     * @return true if the short URL exists, false otherwise.
     */
    public boolean shortUrlExists(String shortUrl) {
        Optional<ShortUrl> surl = shortUrlRepository.findByShortUrl(shortUrl);
        return !surl.isEmpty();
    }

    /**
     * Adds a new short URL entry to the database.
     * 
     * @param url The ShortUrl object to be added.
     * @return The saved ShortUrl object.
     */
    public ShortUrl add(ShortUrl url) {
        ShortUrl savedUrl = shortUrlRepository.save(url);
        return savedUrl;
    }

    /**
     * Updates an existing short URL entry in the database.
     * 
     * @param url The ShortUrl object with updated details.
     * @return The updated ShortUrl object.
     */
    public ShortUrl update(ShortUrl url) {
        shortUrlRepository.save(url);
        return url;
    }

    public void delete(ShortUrl url) {
        shortUrlRepository.deleteById(url.getId());
    }

    /**
     * Retrieves a ShortUrl entry by its ID.
     * 
     * @param id The ID of the short URL entry.
     * @return An Optional containing the ShortUrl object if found.
     */
    public Optional<ShortUrl> getById(Long id) {
        return shortUrlRepository.findById(id);
    }

    /**
     * Retrieves a ShortUrl entry by its short code.
     * 
     * @param code The short code associated with the URL.
     * @return An Optional containing the ShortUrl object if found.
     */
    public Optional<ShortUrl> getByShortCode(String code) {
        return shortUrlRepository.findByShortCode(code);
    }

    /**
     * Retrieves a ShortUrl entry by its short URL.
     * 
     * @param url The shortened URL.
     * @return An Optional containing the ShortUrl object if found.
     */
    public Optional<ShortUrl> getByShortUrl(String url) {
        return shortUrlRepository.findByShortUrl(url);
    }

    /**
     * Retrieves a ShortUrl entry by its full original URL.
     * 
     * @param url The original long URL.
     * @return An Optional containing the ShortUrl object if found.
     */
    public Optional<ShortUrl> getByFullUrl(String url) {
        return shortUrlRepository.findByFullUrl(url);
    }

    /**
     * Retrieves all ShortUrl entries from the database.
     * 
     * @return A list of all stored ShortUrl objects.
     */
    public List<ShortUrl> getAllShortUrls() {
        return (List<ShortUrl>) shortUrlRepository.findAll();
    }
}
