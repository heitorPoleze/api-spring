package edu.ifes.sistema_bibliotecario.dto.emprestimo;

import java.math.BigDecimal;
import java.time.LocalDate;

public record EmprestimoResponseDTO(
        Long id,
        Long livroId,
        String tituloLivro,
        Long leitorId,
        String nomeLeitor,
        LocalDate dataEmprestimo,
        LocalDate dataPrevistaDevolucao,
        LocalDate dataDevolucao,
        BigDecimal multa
) {
}