package com.example.bankcards.controller;

import com.example.bankcards.dto.TransferRequest;
import com.example.bankcards.entity.User;
import com.example.bankcards.security.CustomUserDetails;
import com.example.bankcards.service.TransferService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1")
public class TransferController {
    private final TransferService transferService;

    @PostMapping("/transfers")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void transfers(@AuthenticationPrincipal CustomUserDetails customUserDetails, @Valid @RequestBody TransferRequest transferRequest) {
        User user = customUserDetails.getUser();
        transferService.transfer(user, transferRequest.getFromCardNumber(),
                transferRequest.getToCardNumber(), transferRequest.getAmount());
    }
}
