package com.mkts.waac.Dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class GoalDto {

    private Integer id;

    @NotBlank(message = "Заполните поле")
    @Length(max = 200, message = "Введите менее 200 символов")
    private String name;
}
