package edu.ifes.sistema_bibliotecario.dto.leitor;

import edu.ifes.sistema_bibliotecario.domain.Leitor;
import org.springframework.stereotype.Component;

@Component
public class LeitorMapper {
    public Leitor toDomain(LeitorRequestDTO dto){
        return new Leitor(
                dto.nome(),
                dto.email(),
                dto.tipoPessoa()
        );
    }

    public LeitorResponseDTO toResponse(Leitor leitor) {
        return new LeitorResponseDTO(
                leitor.getId(),
                leitor.getNome(),
                leitor.getEmail(),
                leitor.getTipoPessoa()
        );
    }
}
