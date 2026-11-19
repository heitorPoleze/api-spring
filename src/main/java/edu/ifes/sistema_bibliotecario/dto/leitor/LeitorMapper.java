package edu.ifes.sistema_bibliotecario.dto.leitor;

import edu.ifes.sistema_bibliotecario.domain.Leitor;
import org.springframework.stereotype.Component;

@Component
public class LeitorMapper {
    public Leitor fromRequest(LeitorRequestDTO dto) {
        return Leitor.builder()
                .nome(dto.nome())
                .email(dto.email())
                .tipoPessoa(dto.tipoPessoa())
                .build();
    }

    public LeitorResponseDTO toResponse(Leitor leitor) {
        return new LeitorResponseDTO(
                leitor.getId(),
                leitor.getNome(),
                leitor.getEmail(),
                leitor.getTipoPessoa()
        );
    }

    public LeitorResumoDTO toResumo(Leitor leitor){
        return new LeitorResumoDTO(
                leitor.getId(),
                leitor.getNome()
        );
    }
}
