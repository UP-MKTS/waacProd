package com.mkts.waac.Dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.util.List;

@Getter
@Setter
public class AccompPasspDto {

    private Integer id;

    private String address;

    @NotNull(message = "Заполните правильно (только цифры)")
    @Positive(message = "Введите число больше 0")
    @Max(value = 9999, message = "Введите число меньше 10 000")
    private Integer number;

    @NotBlank(message = "Выберите дату")
    @Pattern(regexp = "[0-9][0-9].[0-9][0-9].[1-2][0-9][0-9][0-9]", message = "Выберите правильную дату")
    private String accompPasspDate;

    @Size(min = 1, message = "Добавьте код отход в список")
    List<AccompPasspDepartmentDto> departmentDtos;

    private Integer contractId;

    private String contractNumber;

    private String contractDate;

    private String recipientOrganizationName;

    private String recipientOrganizationAddress;

    private String wasteDestination;

    private Integer carrierOrganizationId;

    private String carrierOrganizationName;

    private String carrierOrganizationAddress;

    @NotBlank(message = "Выберите дату")
    @Pattern(regexp = "[0-9][0-9].[0-9][0-9].[1-2][0-9][0-9][0-9]", message = "Выберите правильную дату")
    private String transportationDate;

    @NotBlank(message = "Заполните поле")
    @Length(max = 50, message = "Введите менее 50 символов")
    private String carModel;

    @NotBlank(message = "Заполните поле")
    @Length(max = 9, message = "Введите менее 9 символов")
    private String carNumber;

    @NotBlank(message = "Заполните поле")
    @Length(max = 100, message = "Введите менее 100 символов")
    private String driverFio;

    @Length(max = 100, message = "Введите менее 100 символов")
    private String boxing;

    private Double countStored;

    @Size(min = 1, message = "Добавьте код отход в список")
    private List<AccompPasspWasteDto> wasteTypeIdList;

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

    public String getChiefPosition()
    {
        String result = "";
        String flag = "";
        for (AccompPasspDepartmentDto temp:departmentDtos){
            if(flag != temp.getDepartment().getChiefPosition()) {
                result += temp.getDepartment().getChiefPosition() + " ";
            }
            flag = temp.getDepartment().getChiefPosition();
        }
        return result;
    }

    public String getChiefFio()
    {
        String result = "";
        String flag = "";
        for (AccompPasspDepartmentDto temp:departmentDtos){
            if(flag != temp.getDepartment().getChiefFio()) {
                result += temp.getDepartment().getChiefFio() + " ";
            }
            flag = temp.getDepartment().getChiefFio();
        }
        return result;
    }

    public String getWasteCode(){
        String result = "";
        for (AccompPasspWasteDto temp:wasteTypeIdList){
            result += temp.getWasteTypeId().getCode() + " ";

        }
        return result;
    }

}
