package com.munciple.muncipleWebApp.dto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Request {
    private String category;
    private String description;
    private String imageUrl;
    private String location;
    private Long departmentId;
    private Long userId;
    private String longitude;
    private String latitude;
    private Long complaintId;
    private String phoneNumber;
    private String estimatedTimeStr;
    private String status;
    private String remarks;


}
