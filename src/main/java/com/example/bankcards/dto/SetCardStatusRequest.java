package com.example.bankcards.dto;

import com.example.bankcards.entity.Status;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SetCardStatusRequest {
    @NotBlank(message = "Статус не должен быть пустым")
    private Status status;
}
