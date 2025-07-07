package com.munciple.muncipleWebApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComplaintEscalationHistoryDTO {
    private List<EscalationDTO> escalationHistory;
}
