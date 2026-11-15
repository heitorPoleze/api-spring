package edu.ifes.sistema_bibliotecario.dto.livro;

public record LivroResponseDTO(
        long id,
        String titulo,
        String autor,
        String isbn,
        Integer anoPublicacao,
        Integer quantidadeTotal,
        Integer quantidadeDisponivel
) {
}