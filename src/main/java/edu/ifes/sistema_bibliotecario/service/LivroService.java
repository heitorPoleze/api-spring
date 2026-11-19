package edu.ifes.sistema_bibliotecario.service;

import edu.ifes.sistema_bibliotecario.domain.Livro;
import edu.ifes.sistema_bibliotecario.dto.livro.LivroFiltrosDTO;
import edu.ifes.sistema_bibliotecario.dto.livro.LivroMapper;
import edu.ifes.sistema_bibliotecario.dto.livro.LivroRequestDTO;
import edu.ifes.sistema_bibliotecario.exception.NaoEncontradoException;
import edu.ifes.sistema_bibliotecario.exception.RegraDeNegocioVioladaException;
import edu.ifes.sistema_bibliotecario.repository.livro.LivroRepository;
import edu.ifes.sistema_bibliotecario.service.emprestimo.EmprestimoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LivroService {
    private final LivroRepository livroRepository;
    private final LivroMapper livroMapper;
    private final EmprestimoService emprestimoService;

    public Livro save(LivroRequestDTO dto){
        this.livroRepository.findByIsbn(dto.isbn())
                .ifPresent(livro -> {throw new RegraDeNegocioVioladaException("ISBN existente.");
                });

        return this.livroRepository.save(this.livroMapper.toDomain(dto));
    }

    public List<Livro> findByFilters(LivroFiltrosDTO filtros){
        return this.livroRepository.findByFiltro(filtros);
    }

    public Livro findById(long id){
        return this.livroRepository.findById(id)
                .orElseThrow(() -> new NaoEncontradoException("Livro não encontrado"));
    }

    public Livro update(long id, LivroRequestDTO dto){
        Livro livro = this.livroRepository.findById(id)
                .orElseThrow(() -> new NaoEncontradoException("Livro não encontrado"));

        this.livroRepository.findByIsbn(dto.isbn())
                .filter(outroLivro -> outroLivro.getId() != id)
                .ifPresent(outroLivro -> {
                    throw new RegraDeNegocioVioladaException("ISBN já existente.");
                });

        if(dto.quantidadeTotal() < livro.getQuantidadeDisponivel()) throw new RegraDeNegocioVioladaException("Quantidade total não pode ser menor que a quantidade disponível atual.");

        Livro livroComId = this.livroMapper.toDomain(dto);
        livroComId.setId(livro.getId());

        return this.livroRepository.update(livroComId)
                .orElseThrow(() -> new NaoEncontradoException("Livro não encontrado."));
    }

    public void diminuirQtdDisponivel(Livro livro){
        livro.setQuantidadeDisponivel(livro.getQuantidadeDisponivel() - 1);
        this.livroRepository.update(livro);
    }

    public void aumentarQtdDisponivel(Livro livro){
        livro.setQuantidadeDisponivel(livro.getQuantidadeDisponivel() + 1);
        this.livroRepository.update(livro);
    }

    public void deleteById(long id){
        if(this.emprestimoService.isLivroEmprestado(id)) throw new RegraDeNegocioVioladaException("Livro está com empréstimos ativos e não pode ser excluído");
        boolean deletado = this.livroRepository.deleteById(id);
        if(!deletado){
            throw new NaoEncontradoException("Livro não encontrado");
        }
    }
}
