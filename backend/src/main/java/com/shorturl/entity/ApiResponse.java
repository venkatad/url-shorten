package com.shorturl.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponse {
    private int status;
    private String message;
    private Object data;
    
    public static ApiResponse create(int status,String message,Object data){
        ApiResponse apiR=new ApiResponse();
        apiR.setStatus(status);
        apiR.setMessage(message);
        apiR.setData(data);
        return apiR;
    }
}
