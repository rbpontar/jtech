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
public class CreateTaskListRequest {
    @NotBlank(message = "O nome da lista de tarefas é obrigatório")
    @Size(min = 1, max = 100, message = "O nome da lista de tarefas deve ter entre 1 e 100 caracteres")
    private String name;
}
