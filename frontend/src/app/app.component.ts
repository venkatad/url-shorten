import { Component, inject } from '@angular/core';
import {MatInputModule} from '@angular/material/input';
import {MatFormFieldModule} from '@angular/material/form-field';
import {FormsModule} from '@angular/forms';
import {MatButtonModule} from '@angular/material/button';
import {ProgressSpinnerMode, MatProgressSpinnerModule} from '@angular/material/progress-spinner';
import {MatIconModule} from '@angular/material/icon';
import {MatTableModule} from '@angular/material/table';
import { CommonModule } from '@angular/common';
import {MatSnackBar} from '@angular/material/snack-bar';
import { UrlService } from './services/url.service';
import { ShortUrl } from './models/shorturl.model';
import { ApiResponse } from './models/response.model';

@Component({
  selector: 'app-root',
  imports: [CommonModule, FormsModule, MatFormFieldModule, MatInputModule, MatButtonModule, MatProgressSpinnerModule, MatIconModule, MatTableModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {
    fullUrl: string = '';
    shortUrl: string = '';
    gettingShort: boolean = false;
    redirecting: boolean = false;

    shortErrorMessage: string = '';
    shortSuccessUrl: string = '';
    redirectErrorMessage: string = '';

    allMappings: ShortUrl[] = [];
    loadingMappings: boolean = false;

    private _snackBar = inject(MatSnackBar);

    canShowTable: boolean = false;
    pagination: any = {page: 1,totalPages: 0,can_next: false,can_previous: false};

    public constructor(private urlService: UrlService){

    }

    ngOnInit(){
        console.log("ngOnInit");
        this.loadAllMappings();
    }

    getShortUrlClick(){
        console.log("getShortUrlClick: "+this.fullUrl);
        if (!this.isUrlValid(this.fullUrl)){
            this.shortSuccessUrl='';
            this.shortErrorMessage="Please enter a valid url";
            return;
        }
        this.getShortUrl();
    }

    async getShortUrl(){
        this.shortSuccessUrl='';
        this.shortErrorMessage='';
        this.gettingShort=true;
        this.urlService.createShortUrl(this.fullUrl).subscribe({
            next: (obj: ApiResponse)=>{
                console.log(obj);
                this.gettingShort=false;
                if (obj.status==200){
                    this.shortSuccessUrl=obj.data;
                    this.shortErrorMessage='';
                    this.loadAllMappings();
                    this.fullUrl='';
                }else{
                    this.shortErrorMessage=obj.message;
                }
            },
            error: (err: any)=>{
                this.gettingShort=false;
                this.shortErrorMessage="Unable to connect to the backend";
            }
        }); 
    }

    redirectClick(){
        console.log("redirectClick: "+this.shortUrl);
        this.getForRedirect();
    }

    async getForRedirect(){
        this.redirectErrorMessage='';
        this.redirecting=true;
        this.urlService.getFullUrl(this.shortUrl).subscribe({
            next: (obj: ApiResponse)=>{
                console.log(obj);
                this.redirecting=false;
                if (obj.status==200){
                    this.shortUrl='';
                    let redUrl=obj.data;
                    if (redUrl.indexOf("http://")==-1 && redUrl.indexOf("https://")==-1){
                        redUrl="https://"+redUrl;
                    }
                    window.open(redUrl,"_blank");
                }else{
                    this.redirectErrorMessage=obj.message;
                }
            },error: (err: any)=>{
                console.error(err);
                this.redirecting=false;
                this.redirectErrorMessage="Unable to connect to the backend";
            }
        });
    }

    copyToClipboardClick(){
        console.log("copyToClipboardClick: "+this.shortSuccessUrl);
        navigator.clipboard.writeText(this.shortSuccessUrl);
        this._snackBar.open("Short url copied to clipboard","",{duration: 3000});
    }

    async loadAllMappings(){
        this.loadingMappings=true;
        this.urlService.getAllUrls(this.pagination.page.toString()).subscribe({
            next: (obj: ApiResponse)=>{
                console.log(obj);
                this.loadingMappings=false;
                if (obj.status==200){
                    this.pagination.page=obj.data.page;
                    this.pagination.totalPages=obj.data.totalPages;
                    let urls = obj.data.urls as ShortUrl[];
                    this.allMappings=[];
                    for (let a=0;a<urls.length;a++){
                        let val=urls[a];
                        this.allMappings.push(val);
                    }
                    this.pagination.can_previous=obj.data.page>1;
                    this.pagination.can_next=obj.data.page<obj.data.totalPages
                }else{
                }
                this.canShowTable=this.allMappings.length>0;
            },error: (err: any)=>{
                console.error(err);
                this.loadingMappings=false;
            }
        });
    }

    isUrlValid(url: string) {
        var pattern = /(http(s)?:\/\/.)?(www\.)?[-a-zA-Z0-9@:%._\+~#=]{2,256}\.[a-z]{2,6}\b([-a-zA-Z0-9@:%_\+.~#?&//=]*)/g;
        var res = url.match(pattern);
        if(res == null)
            return false;
        else
            return true;
    }

    pageNextClick(){
        this.pagination.page+=1;
        this.loadAllMappings();
    }

    pagePreviousClick(){
        this.pagination.page-=1;
        this.loadAllMappings();
    }
}
