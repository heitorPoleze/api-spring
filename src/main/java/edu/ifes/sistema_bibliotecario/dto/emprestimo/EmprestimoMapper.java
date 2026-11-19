package edu.ifes.sistema_bibliotecario.dto.emprestimo;

import edu.ifes.sistema_bibliotecario.domain.Emprestimo;
import edu.ifes.sistema_bibliotecario.domain.Leitor;
import edu.ifes.sistema_bibliotecario.domain.Livro;
import edu.ifes.sistema_bibliotecario.dto.leitor.LeitorMapper;
import edu.ifes.sistema_bibliotecario.dto.leitor.LeitorResumoDTO;
import edu.ifes.sistema_bibliotecario.dto.livro.LivroMapper;
import edu.ifes.sistema_bibliotecario.dto.livro.LivroResumoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class EmprestimoMapper {
    private final LivroMapper livroMapper;
    private final LeitorMapper leitorMapper;

    public Emprestimo toDomain(EmprestimoRequestDTO dto) {
        return Emprestimo.builder()
                .leitorId(dto.leitorId())
                .livroId(dto.livroId())
                .build();
    }

    public EmprestimoResponseDTO toResponse(Emprestimo emprestimo, Livro livro, Leitor leitor) {
        return new EmprestimoResponseDTO(
                emprestimo.getId(),
                this.livroMapper.toResumo(livro),
                this.leitorMapper.toResumo(leitor),
                emprestimo.getDataEmprestimo(),
                emprestimo.getDataPrevistaDevolucao(),
                emprestimo.getDataDevolucao(),
                emprestimo.getMulta()
        );
    }
}