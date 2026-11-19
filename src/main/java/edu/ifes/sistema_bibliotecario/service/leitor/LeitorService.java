package edu.ifes.sistema_bibliotecario.service.leitor;

import edu.ifes.sistema_bibliotecario.domain.Leitor;
import edu.ifes.sistema_bibliotecario.domain.TipoPessoa;
import edu.ifes.sistema_bibliotecario.dto.leitor.LeitorMapper;
import edu.ifes.sistema_bibliotecario.dto.leitor.LeitorRequestDTO;
import edu.ifes.sistema_bibliotecario.exception.NaoEncontradoException;
import edu.ifes.sistema_bibliotecario.exception.RegraDeNegocioVioladaException;
import edu.ifes.sistema_bibliotecario.repository.leitor.LeitorRepository;
import edu.ifes.sistema_bibliotecario.service.emprestimo.EmprestimoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LeitorService {
    private final LeitorRepository leitorRepository;
    private final LeitorMapper leitorMapper;
    private final EmprestimoService emprestimoService;

    public Leitor save(LeitorRequestDTO dto) {
        this.leitorRepository.findByEmail(dto.email())
                .ifPresent(leitorExistente -> {
                    throw new RegraDeNegocioVioladaException("Leitor não pode ser cadastrado.");
                });

        return this.leitorRepository.save(this.leitorMapper.fromRequest(dto));
    }

    public List<Leitor> findAll(TipoPessoa tipo) {
        return (tipo != null)
                ? this.leitorRepository.findByTipo(tipo)
                : this.leitorRepository.findAll();
    }

    public Leitor findById(long id) {
        return this.leitorRepository.findById(id)
                .orElseThrow(() -> new NaoEncontradoException("Leitor não encontrado."));
    }

    public Leitor update(long id, LeitorRequestDTO dto){
        this.leitorRepository.findById(id)
                .orElseThrow(() -> new NaoEncontradoException("Leitor não encontrado."));

        this.leitorRepository.findByEmail(dto.email())
                .filter(outroLeitor -> outroLeitor.getId() != id)
                .ifPresent(outroLeitor -> {
                    throw new RegraDeNegocioVioladaException("E-mail já cadastrado.");
                });

        Leitor leitorComId = this.leitorMapper.fromRequest(dto);
        leitorComId.setId(id);

        return this.leitorRepository.update(leitorComId)
                .orElseThrow(() -> new NaoEncontradoException("Leitor não encontrado."));
    }

    public void deleteById(long id){
        if(this.emprestimoService.isLeitorComEmprestimoAtivo(id)) throw new RegraDeNegocioVioladaException("Leitor não pode ser excluído pois está com empréstimos ativos.");
        boolean deletado = this.leitorRepository.deleteById(id);
        if(!deletado){
            throw new NaoEncontradoException("Leitor não encontrado");
        }
    }
}
