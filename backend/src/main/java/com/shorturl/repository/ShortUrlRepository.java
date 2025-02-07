package com.shorturl.repository;

import com.shorturl.entity.ShortUrl;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface ShortUrlRepository extends CrudRepository<ShortUrl,Long>{
    Optional<ShortUrl> findByShortCode(String code);
    Optional<ShortUrl> findByFullUrl(String url);
    Optional<ShortUrl> findByShortUrl(String url);
}
