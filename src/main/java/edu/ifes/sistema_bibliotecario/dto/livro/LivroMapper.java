package edu.ifes.sistema_bibliotecario.dto.livro;

import edu.ifes.sistema_bibliotecario.domain.Livro;
import org.springframework.stereotype.Component;

@Component
public class LivroMapper {

    public Livro toEntity(LivroRequestDTO dto) {
        Livro livro = new Livro(
                dto.titulo(),
                dto.autor(),
                dto.isbn(),
                dto.anoPublicacao(),
                dto.quantidadeTotal()
        );
        livro.setQuantidadeDisponivel(dto.quantidadeTotal());
        return livro;
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
}