package edu.ifes.sistema_bibliotecario.dto.emprestimo;

import edu.ifes.sistema_bibliotecario.dto.leitor.LeitorResumoDTO;
import edu.ifes.sistema_bibliotecario.dto.livro.LivroResumoDTO;

import java.math.BigDecimal;
import java.time.LocalDate;

public record EmprestimoResponseDTO(
        Long id,
        LivroResumoDTO livro,
        LeitorResumoDTO leitor,
        LocalDate dataEmprestimo,
        LocalDate dataPrevistaDevolucao,
        LocalDate dataDevolucao,
        BigDecimal multa
) {
}