package edu.ifes.sistema_bibliotecario.repository.livro;

import edu.ifes.sistema_bibliotecario.domain.Livro;
import edu.ifes.sistema_bibliotecario.domain.especificacao.Especificacao;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class LivroRepositoryMemoria implements LivroRepository {
    private final List<Livro> banco = new ArrayList<>();
    private long contador = 0;

    @Override
    public Livro save(Livro livro){
        livro.setId(this.incrementarId());
        this.banco.add(livro);
        return livro;
    }

    @Override
    public List<Livro> findAll(Especificacao<Livro> spec){
        return this.banco.stream()
                .filter(spec::atende)
                .toList();
    }

    @Override
    public Optional<Livro> findById(long id){
        return this.banco.stream()
                .filter(livro -> livro.getId() == id)
                .findFirst();
    }

    @Override
    public Optional<Livro> findByIsbn(String isbn){
        return this.banco.stream()
                .filter(livro -> livro.getIsbn().equals(isbn))
                .findFirst();
    }

    @Override
    public Optional<Livro> update(Livro livro){
        for (int i = 0; i < this.banco.size(); i++) {
            if (this.banco.get(i).getId() == livro.getId()) {
                this.banco.set(i, livro);
                return Optional.of(livro);
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean deleteById(long id){
        return this.banco.removeIf(livro -> livro.getId() == id);
    }

    private long incrementarId(){
        return ++contador;
    }
}
