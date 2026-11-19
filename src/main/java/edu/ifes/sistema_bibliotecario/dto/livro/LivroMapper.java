package edu.ifes.sistema_bibliotecario.dto.livro;

import edu.ifes.sistema_bibliotecario.domain.Livro;
import org.springframework.stereotype.Component;

@Component
public class LivroMapper {

    public Livro toDomain(LivroRequestDTO dto) {
        return Livro.builder()
                .titulo(dto.titulo())
                .autor(dto.autor())
                .isbn(dto.isbn())
                .anoPublicacao(dto.anoPublicacao())
                .quantidadeTotal(dto.quantidadeTotal())
                .build();
    }

    public LivroResponseDTO toResponse(Livro livro) {
        return new LivroResponseDTO(
                livro.getId(),
                livro.getTitulo(),
                livro.getAutor(),
                livro.getIsbn(),
                livro.getAnoPublicacao(),
                livro.getQuantidadeTotal(),
                livro.getQuantidadeDisponivel()
        );
    }

    public LivroResumoDTO toResumo(Livro livro){
        return new LivroResumoDTO(
                livro.getId(),
                livro.getTitulo()
        );
    }
}