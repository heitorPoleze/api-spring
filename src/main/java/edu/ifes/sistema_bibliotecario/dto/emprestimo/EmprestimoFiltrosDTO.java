package edu.ifes.sistema_bibliotecario.dto.emprestimo;

public record EmprestimoFiltrosDTO(
        long leitorId,
        long livroId,
        boolean ativos
) {
}
