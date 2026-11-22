package edu.ifes.sistema_bibliotecario.domain.especificacao;

import edu.ifes.sistema_bibliotecario.domain.Emprestimo;

import java.util.Objects;

public final class EmprestimoEspecificacoes {

    private EmprestimoEspecificacoes() {
    }

    public static Especificacao<Emprestimo> doLeitor(long leitorId) {
        return emprestimo -> Objects.equals(emprestimo.getLeitorId(), leitorId);
    }

    public static Especificacao<Emprestimo> ativo(boolean valor) {
        return emprestimo -> emprestimo.isAtivo() == valor;
    }
}
