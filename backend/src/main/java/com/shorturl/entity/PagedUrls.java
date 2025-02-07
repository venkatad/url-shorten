package com.shorturl.entity;

import java.util.List;
import java.util.Vector;

public class PagedUrls {
    public int page;
    public int recordsPerPage;
    public int totalPages;
    public List<ShortUrl> urls=new Vector<>();
}
