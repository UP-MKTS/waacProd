package com.mkts.waac.Dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AccompPasspJournalDto {

    private Integer id;

    private Integer accompPasspNumber;

    List<AccompPasspDepartmentDto> departmentDtos;

    private String accompPasspDate;

    private String transportationDate;

    private String contractNumber;

    private String contractDate;

    private String recipientOrganizationName;

    private String wasteDangerousClassName;

    private List<AccompPasspWasteDto> accompPasspWasteDtoList;

    public String getDepartmentsShortName()
    {
        String result = "";
        String flag = "";
        for (AccompPasspDepartmentDto temp:departmentDtos){
            if(flag != temp.getDepartment().getShortName()){
                result+=temp.getDepartment().getShortName()+" ";
            }
            flag = temp.getDepartment().getShortName();
        }
        return result;
    }

}
