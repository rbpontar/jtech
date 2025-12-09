package com.sample.poc.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateTaskRequest {

    @NotBlank(message = "O título é obrigatório")
    @Size(min = 1, max = 255, message = "O título deve ter entre 1 e 255 caracteres")
    private String title;

    @Size(max = 2000, message = "A descrição não pode exceder 2000 caracteres")
    private String description;

    private Long taskListId;
}
