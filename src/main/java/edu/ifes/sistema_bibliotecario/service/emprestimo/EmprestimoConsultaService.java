package edu.ifes.sistema_bibliotecario.service.emprestimo;

import edu.ifes.sistema_bibliotecario.repository.emprestimo.EmprestimoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmprestimoConsultaService {
    private final EmprestimoRepository emprestimoRepository;

    public boolean isLivroEmprestado(long idLivro){
        return !this.emprestimoRepository.findAtivosByLivroId(idLivro).isEmpty();
    }

    public boolean isLeitorComEmprestimoAtivo(long idLeitor){
        return !this.emprestimoRepository.findAtivosByLeitorId(idLeitor).isEmpty();
    }
}
