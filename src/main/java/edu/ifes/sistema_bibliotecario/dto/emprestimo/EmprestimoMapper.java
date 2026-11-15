package edu.ifes.sistema_bibliotecario.dto.emprestimo;

import edu.ifes.sistema_bibliotecario.domain.Emprestimo;
import edu.ifes.sistema_bibliotecario.domain.Leitor;
import edu.ifes.sistema_bibliotecario.domain.Livro;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class EmprestimoMapper {

    public Emprestimo toDomain(
            EmprestimoRequestDTO dto,
            LocalDate dataEmprestimo,
            LocalDate dataPrevistaDevolucao
    ) {
        return Emprestimo.builder(dto.livroId(), dto.leitorId())
                .dataEmprestimo(dataEmprestimo)
                .dataPrevistaDevolucao(dataPrevistaDevolucao)
                .build();
    }

    public EmprestimoResponseDTO toResponse(Emprestimo emprestimo, Livro livro, Leitor leitor) {
        return new EmprestimoResponseDTO(
                emprestimo.getId(),
                emprestimo.getLivroId(),
                livro.getTitulo(),
                emprestimo.getLeitorId(),
                leitor.getNome(),
                emprestimo.getDataEmprestimo(),
                emprestimo.getDataPrevistaDevolucao(),
                emprestimo.getDataDevolucao(),
                emprestimo.getMulta()
        );
    }
}