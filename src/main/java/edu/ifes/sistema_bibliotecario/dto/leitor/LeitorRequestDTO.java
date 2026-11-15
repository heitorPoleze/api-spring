package edu.ifes.sistema_bibliotecario.dto.leitor;

import edu.ifes.sistema_bibliotecario.domain.TipoPessoa;
import edu.ifes.sistema_bibliotecario.dto.ValidationConstants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record LeitorRequestDTO(
    @NotBlank(message = "O nome é obrigatório")
    @Size(max = ValidationConstants.NOME_MAX, message = "O nome deve ter no máximo" +
            ValidationConstants.NOME_MAX + " caracteres")
    String nome,

    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "Formato de e-mail inválido")
    String email,

    @NotNull
    TipoPessoa tipoPessoa
){}