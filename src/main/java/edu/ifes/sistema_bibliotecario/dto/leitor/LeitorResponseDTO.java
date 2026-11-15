package edu.ifes.sistema_bibliotecario.dto.leitor;
import edu.ifes.sistema_bibliotecario.domain.TipoPessoa;

public record LeitorResponseDTO(
        long id,
        String nome,
        String email,
        TipoPessoa tipoPessoa
) {
}

