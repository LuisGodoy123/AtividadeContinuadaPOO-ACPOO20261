package br.edu.cs.poo.ac.bolsa.dao;

import br.edu.cs.poo.ac.bolsa.entidade.Titulo;

public class TituloDAO extends DAOGenerico {
    public TituloDAO() {
        inicializarCadastro(Titulo.class);
    }

    public Titulo buscar(String numero) {
        return (Titulo) cadastro.buscar(numero);
    }

    public boolean incluir(Titulo titulo) {
        if (buscar(titulo.getNumero()) == null) {
            cadastro.incluir(titulo, titulo.getNumero());
            return true;
        } else {
            return false;
        }
    }

    public boolean alterar(Titulo titulo) {
        if (buscar(titulo.getNumero()) != null) {
            cadastro.alterar(titulo, titulo.getNumero());
            return true;
        } else {
            return false;
        }
    }

    public boolean excluir(String numero) {
        if (buscar(numero) != null) {
            cadastro.excluir(numero);
            return true;
        } else {
            return false;
        }
    }
}