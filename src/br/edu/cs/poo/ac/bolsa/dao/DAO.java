package br.edu.cs.poo.ac.bolsa.dao;

import java.io.Serializable;
import java.lang.reflect.Array;

import br.edu.cs.poo.ac.bolsa.util.ExcecaoObjetoJaExistente;
import br.edu.cs.poo.ac.bolsa.util.ExcecaoOobjetoNaoExistente;
import br.edu.cs.poo.ac.bolsa.util.Registro;

public class DAO<T extends Registro> extends DAOGenerico {

    private Class<T> classe;

    public DAO(Class<T> classe) {
        this.classe = classe;
        inicializarCadastro(classe);
    }

    public T buscar(String identificador) {
        return (T) cadastro.buscar(identificador);
    }

    public void incluir(T registro) throws ExcecaoObjetoJaExistente {
        if (buscar(registro.getIdentificador()) != null) {
            throw new ExcecaoObjetoJaExistente();
        }
        cadastro.incluir(registro, registro.getIdentificador());
    }

    public void alterar(T registro) throws ExcecaoOobjetoNaoExistente {
        if (buscar(registro.getIdentificador()) == null) {
            throw new ExcecaoOobjetoNaoExistente();
        }
        cadastro.alterar(registro, registro.getIdentificador());
    }

    public void excluir(String identificador) throws ExcecaoOobjetoNaoExistente {
        if (buscar(identificador) == null) {
            throw new ExcecaoOobjetoNaoExistente();
        }
        cadastro.excluir(identificador);
    }

    public T[] buscarTodos() {
        Serializable[] todos = cadastro.buscarTodos();
        if (todos == null) return null;

        @SuppressWarnings("unchecked")
        T[] resultado = (T[]) Array.newInstance(classe, todos.length);
        for (int i = 0; i < todos.length; i++) {
            resultado[i] = (T) todos[i];
        }
        return resultado;
    }
}