package br.edu.cs.poo.ac.bolsa.dao;

import br.edu.cs.poo.ac.bolsa.entidades.Ativo;

public class DAOAtivo extends DAOGenerico {
    public DAOAtivo() {
        inicializarCadastro(Ativo.class);
    }

    public Ativo buscar(long codigo) {
        return (Ativo) cadastro.buscar("" + codigo);
    }

    public boolean incluir(Ativo ativo) {
        if (buscar(ativo.getCodigo()) == null) {
            cadastro.incluir(ativo, "" + ativo.getCodigo());
            return true;
        } else {
            return false;
        }
    }

    public boolean alterar(Ativo ativo) {
        if (buscar(ativo.getCodigo()) != null) {
            cadastro.alterar(ativo, "" + ativo.getCodigo());
            return true;
        } else {
            return false;
        }
    }

    public boolean excluir(long codigo) {
        if (buscar(codigo) != null) {
            cadastro.excluir("" + codigo);
            return true;
        } else {
            return false;
        }
    }
}