package br.edu.cs.poo.ac.bolsa.dao;

import br.edu.cs.poo.ac.bolsa.entidade.Ativo;

public class DAOAtivo extends DAORegistro {

    public DAOAtivo() {
        super(Ativo.class);
    }

    public Ativo buscar(long codigo) {
        return (Ativo) super.buscar("" + codigo);
    }

    public boolean incluir(Ativo ativo) {
        return super.incluir(ativo);
    }

    public boolean alterar(Ativo ativo) {
        return super.alterar(ativo);
    }

    public boolean excluir(long codigo) {
        return super.excluir("" + codigo);
    }
}