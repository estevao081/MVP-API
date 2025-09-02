package com.example.neoProjectAPI.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public record ClientRecordDTO(
        String name,
        String mail,
        String password,
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate birthday) {
}
