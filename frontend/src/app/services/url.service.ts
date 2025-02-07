import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { ApiResponse } from "../models/response.model";

@Injectable({providedIn: 'root'})
export class UrlService {

    private API_BASE_URL: string = 'http://localhost:8080/';

    constructor(private http: HttpClient) {
    }

    getAllUrls(page: string){
        let url = this.API_BASE_URL+"all?page="+page;
        console.log("url: "+url);
        return this.http.get<ApiResponse>(url);
    }

    createShortUrl(fullUrl: string){
        let encodedUrl = encodeURIComponent(fullUrl);
        let url = this.API_BASE_URL+"create?url="+encodedUrl;
        console.log(url);
        return this.http.get<ApiResponse>(url);
    }

    getFullUrl(shortUrl: string){
        let url = shortUrl;
        console.log(url);
        return this.http.get<ApiResponse>(url);
    }
}