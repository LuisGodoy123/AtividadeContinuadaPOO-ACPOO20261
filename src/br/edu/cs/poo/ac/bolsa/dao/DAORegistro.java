package br.edu.cs.poo.ac.bolsa.dao;

import br.edu.cs.poo.ac.bolsa.util.Registro;

public class DAORegistro extends DAOGenerico {

    public DAORegistro(Class<?> classe) {
        inicializarCadastro(classe);
    }

    public Registro buscar(String identificador) {
        return (Registro) cadastro.buscar(identificador);
    }

    public boolean incluir(Registro registro) {
        if (buscar(registro.getIdentificador()) == null) {
            cadastro.incluir(registro, registro.getIdentificador());
            return true;
        }
        return false;
    }

    public boolean alterar(Registro registro) {
        if (buscar(registro.getIdentificador()) != null) {
            cadastro.alterar(registro, registro.getIdentificador());
            return true;
        }
        return false;
    }

    public boolean excluir(String identificador) {
        if (buscar(identificador) != null) {
            cadastro.excluir(identificador);
            return true;
        }
        return false;
    }
}