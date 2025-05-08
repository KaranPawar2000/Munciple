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



}
