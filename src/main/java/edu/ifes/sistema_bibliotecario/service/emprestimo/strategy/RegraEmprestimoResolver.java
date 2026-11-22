package edu.ifes.sistema_bibliotecario.service.emprestimo.strategy;

import edu.ifes.sistema_bibliotecario.domain.TipoPessoa;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class RegraEmprestimoResolver {

    private final Map<TipoPessoa, RegraEmprestimoStrategy> regrasPorTipo;

    public RegraEmprestimoResolver(List<RegraEmprestimoStrategy> regras) {
        this.regrasPorTipo = new EnumMap<>(TipoPessoa.class);

        for (RegraEmprestimoStrategy regra : regras) {
            TipoPessoa tipo = regra.tipo();

            if (this.regrasPorTipo.containsKey(tipo)) {
                throw new IllegalStateException("Mais de uma regra de empréstimo para o tipo " + tipo);
            }

            this.regrasPorTipo.put(tipo, regra);
        }
    }

    public RegraEmprestimoStrategy para(TipoPessoa tipoPessoa) {
        return Optional.ofNullable(this.regrasPorTipo.get(tipoPessoa))
                .orElseThrow(() -> new IllegalStateException("Nenhuma regra de empréstimo para o tipo " + tipoPessoa));
    }
}
