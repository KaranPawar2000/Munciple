package com.munciple.muncipleWebApp.dto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.munciple.muncipleWebApp.entity.User;
import lombok.Data;

import java.util.List;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {
  private String message;
  private User user;
  private String status;
  private String userName;
  private List<DepartmentDTO> departments;
  private long userId;
  private String wardNumber;
  private String MarathiName;

  private List<OfficerInfo> officers;

  @Data
  public static class OfficerInfo {
    private Long officerId;
    private String name;
    private String phoneNumber;
    private String email;
    private String role;
    private String assignedZone;
    private String departmentName;
    private Long dept_id;
    private java.time.LocalDateTime createdAt;
  }

}
