package com.example.bankcards.dto.Request;

import com.example.bankcards.entity.Role;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRoleRequest {
    @NotNull(message = "Роль обязательна")
    private Role role;
}
