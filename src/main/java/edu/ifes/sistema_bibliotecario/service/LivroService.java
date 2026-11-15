package edu.ifes.sistema_bibliotecario.service;

import edu.ifes.sistema_bibliotecario.domain.Livro;
import edu.ifes.sistema_bibliotecario.dto.livro.LivroFiltrosDTO;
import edu.ifes.sistema_bibliotecario.dto.livro.LivroMapper;
import edu.ifes.sistema_bibliotecario.dto.livro.LivroRequestDTO;
import edu.ifes.sistema_bibliotecario.dto.livro.LivroResponseDTO;
import edu.ifes.sistema_bibliotecario.exception.NaoEncontradoException;
import edu.ifes.sistema_bibliotecario.exception.RegraDeNegocioVioladaException;
import edu.ifes.sistema_bibliotecario.repository.livro.LivroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LivroService {
    private final LivroRepository livroRepository;
    private final LivroMapper livroMapper;

    public LivroResponseDTO save(LivroRequestDTO dto){
        this.livroRepository.findByIsbn(dto.isbn())
                .ifPresent(livro -> {throw new RegraDeNegocioVioladaException("ISBN existente.");
                });

        Livro livroSalvo = this.livroRepository.save(this.livroMapper.toDomain(dto));
        return this.livroMapper.toResponse(livroSalvo);
    }

    public List<LivroResponseDTO> findByFilters(LivroFiltrosDTO filtros){
        List<Livro> livros = this.livroRepository.findByFiltro(filtros);
        return livros.stream()
                .map(this.livroMapper::toResponse)
                .toList();
    }

    public LivroResponseDTO findById(long id){
        Livro livro = this.livroRepository.findById(id)
                .orElseThrow(() -> new NaoEncontradoException("Livro não encontrado"));
        return this.livroMapper.toResponse(livro);
    }

    public LivroResponseDTO update(long id, LivroRequestDTO dto){
        this.livroRepository.findById(id)
                .orElseThrow(() -> new NaoEncontradoException("Livro não encontrado"));

        this.livroRepository.findByIsbn(dto.isbn())
                .filter(outroLivro -> outroLivro.getId() != id)
                .ifPresent(outroLivro -> {
                    throw new RegraDeNegocioVioladaException("ISBN já existente.");
                });

        Livro livroComId = this.livroMapper.toDomain(dto);
        livroComId.setId(id);

        Livro livroAtualizado = this.livroRepository.update(livroComId)
                .orElseThrow(() -> new NaoEncontradoException("Livro não encontrado."));

        return this.livroMapper.toResponse(livroAtualizado);
    }

    public void deleteById(long id){
        boolean deletado = this.livroRepository.deleteById(id);
        if(!deletado){
            throw new NaoEncontradoException("Livro não encontrado");
        }
    }
}
