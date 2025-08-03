package com.example.bankcards.dto.Request;

import com.example.bankcards.entity.Status;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SetCardStatusRequest {
    @NotNull(message = "Статус не должен быть пустым")
    private Status status;
}
