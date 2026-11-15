package edu.ifes.sistema_bibliotecario.dto.livro;

import jakarta.validation.constraints.*;

import static edu.ifes.sistema_bibliotecario.dto.ValidationConstants.TITULO_MAX;
import static edu.ifes.sistema_bibliotecario.dto.ValidationConstants.NOME_MAX;
import static edu.ifes.sistema_bibliotecario.dto.ValidationConstants.ANO_PUBLICACAO_MIN;
import static edu.ifes.sistema_bibliotecario.dto.ValidationConstants.ISBN_LENGTH;

public record LivroRequestDTO(
        @NotBlank(message = "O título é obrigatório")
        @Size(max = TITULO_MAX, message = "O título deve ter no máximo " + TITULO_MAX + " caracteres")
        String titulo,

        @NotBlank(message = "O autor é obrigatório")
        @Size(max = NOME_MAX, message = "O autor deve ter no máximo " + NOME_MAX + " caracteres")
        String autor,

        @NotBlank(message = "O ISBN é obrigatório")
        @Pattern(regexp = "\\d{" + ISBN_LENGTH + "}", message = "O ISBN deve conter exatamente " + ISBN_LENGTH + " dígitos")
        String isbn,

        @NotNull(message = "O ano de publicação é obrigatório")
        @Min(value = ANO_PUBLICACAO_MIN, message = "O ano de publicação deve ser a partir de " + ANO_PUBLICACAO_MIN)
        Integer anoPublicacao,

        @NotNull(message = "A quantidade total é obrigatória")
        @Positive(message = "A quantidade total deve ser maior que zero")
        Integer quantidadeTotal
) {
}
