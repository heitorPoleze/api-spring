package edu.ifes.sistema_bibliotecario.domain.especificacao;

@FunctionalInterface
public interface Especificacao<T> {

    boolean atende(T t);

    default Especificacao<T> and(Especificacao<T> outra) {
        return t -> this.atende(t) && outra.atende(t);
    }

    default Especificacao<T> or(Especificacao<T> outra) {
        return t -> this.atende(t) || outra.atende(t);
    }

    default Especificacao<T> not() {
        return t -> !this.atende(t);
    }

    static <T> Especificacao<T> todos() {
        return t -> true;
    }
}
