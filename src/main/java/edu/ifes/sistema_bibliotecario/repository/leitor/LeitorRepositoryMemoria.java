package edu.ifes.sistema_bibliotecario.repository.leitor;

import edu.ifes.sistema_bibliotecario.domain.Leitor;
import edu.ifes.sistema_bibliotecario.domain.especificacao.Especificacao;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class LeitorRepositoryMemoria implements LeitorRepository{
    private final List<Leitor> banco = new ArrayList<>();
    private long contador = 0;


    @Override
    public Leitor save(Leitor leitor){
        leitor.setId(this.incrementarId());
        this.banco.add(leitor);
        return leitor;
    }

    @Override
    public List<Leitor> findAll(){
        return List.copyOf(this.banco);
    }

    @Override
    public List<Leitor> findAll(Especificacao<Leitor> spec){
        return this.banco.stream()
                .filter(spec::atende)
                .toList();
    }

    @Override
    public Optional<Leitor> findById(long id){
        return this.banco.stream()
                .filter(leitor -> leitor.getId() == id)
                .findFirst();
    }

    @Override
    public Optional<Leitor> findByEmail(String email){
        return this.banco.stream()
                .filter(leitor -> leitor.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public Optional<Leitor> update(Leitor leitor) {
        for (int i = 0; i < this.banco.size(); i++) {
            if (this.banco.get(i).getId() == leitor.getId()) {
                this.banco.set(i, leitor);
                return Optional.of(leitor);
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean deleteById(long id){
        return this.banco.removeIf(leitor -> leitor.getId() == id);
    }

    private long incrementarId(){
        return ++contador;
    }
}
