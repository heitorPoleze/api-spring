package edu.ifes.sistema_bibliotecario.service;

import edu.ifes.sistema_bibliotecario.domain.Leitor;
import edu.ifes.sistema_bibliotecario.domain.TipoPessoa;
import edu.ifes.sistema_bibliotecario.dto.leitor.LeitorMapper;
import edu.ifes.sistema_bibliotecario.dto.leitor.LeitorRequestDTO;
import edu.ifes.sistema_bibliotecario.dto.leitor.LeitorResponseDTO;
import edu.ifes.sistema_bibliotecario.exception.NaoEncontradoException;
import edu.ifes.sistema_bibliotecario.exception.RegraDeNegocioVioladaException;
import edu.ifes.sistema_bibliotecario.repository.leitor.LeitorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LeitorService {
    private final LeitorRepository leitorRepository;
    private final LeitorMapper leitorMapper;

    public LeitorResponseDTO save(LeitorRequestDTO dto) {
        this.leitorRepository.findByEmail(dto.email())
                .ifPresent(leitorExistente -> {
                    throw new RegraDeNegocioVioladaException("Leitor não pode ser cadastrado.");
                });

        Leitor leitorSalvo = this.leitorRepository.save(this.leitorMapper.toDomain(dto));
        return this.leitorMapper.toResponse(leitorSalvo);
    }

    public List<LeitorResponseDTO> findAll(TipoPessoa tipo) {
        List<Leitor> leitores = (tipo != null)
                ? this.leitorRepository.findByTipo(tipo)
                : this.leitorRepository.findAll();

        return leitores.stream()
                .map(this.leitorMapper::toResponse)
                .toList();
    }

    public LeitorResponseDTO findById(long id) {
        Leitor leitor = this.leitorRepository.findById(id)
                .orElseThrow(() -> new NaoEncontradoException("Leitor não encontrado."));
        return this.leitorMapper.toResponse(leitor);
    }

    public LeitorResponseDTO update(long id, LeitorRequestDTO dto){
        this.leitorRepository.findById(id)
                .orElseThrow(() -> new NaoEncontradoException("Leitor não encontrado."));

        this.leitorRepository.findByEmail(dto.email())
                .filter(outroLeitor -> outroLeitor.getId() != id)
                .ifPresent(outroLeitor -> {
                    throw new RegraDeNegocioVioladaException("E-mail já cadastrado.");
                });

        Leitor leitorComId = this.leitorMapper.toDomain(dto);
        leitorComId.setId(id);

        Leitor leitorAtualizado = this.leitorRepository.update(leitorComId)
                .orElseThrow(() -> new NaoEncontradoException("Leitor não encontrado."));

        return this.leitorMapper.toResponse(leitorAtualizado);
    }

    public void deleteById(long id){
        boolean deletado = this.leitorRepository.deleteById(id);
        if(!deletado){
            throw new NaoEncontradoException("Leitor não encontrado");
        }
    }
}
