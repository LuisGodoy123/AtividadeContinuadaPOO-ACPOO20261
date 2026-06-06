package br.edu.cs.poo.ac.bolsa.dao;

import br.edu.cs.poo.ac.bolsa.entidade.InvestidorPessoa;

public class DAOInvestidorPessoa extends DAOGenerico {
    public DAOInvestidorPessoa() {
        inicializarCadastro(InvestidorPessoa.class);
    }

    public InvestidorPessoa buscarInvestidorPessoa(String cpf) {
        return (InvestidorPessoa) cadastro.buscar(cpf);
    }

    public boolean incluirInvestidorPessoa(InvestidorPessoa ip) {
        if (buscarInvestidorPessoa(ip.getCpf()) == null) {
            cadastro.incluir(ip, ip.getCpf());
            return true;
        } else {
            return false;
        }
    }

    public boolean alterarInvestidorPessoa(InvestidorPessoa ip) {
        if (buscarInvestidorPessoa(ip.getCpf()) != null) {
            cadastro.alterar(ip, ip.getCpf());
            return true;
        } else {
            return false;
        }
    }

    public boolean excluirInvestidorPessoa(String cpf) {
        if (buscarInvestidorPessoa(cpf) != null) {
            cadastro.excluir(cpf);
            return true;
        } else {
            return false;
        }
    }
    
    public InvestidorPessoa[] consultarTodos() {
        java.io.Serializable[] todos = cadastro.buscarTodos();
        if (todos == null) return null;
        InvestidorPessoa[] resultado = new InvestidorPessoa[todos.length];
        for (int i = 0; i < todos.length; i++) {
            resultado[i] = (InvestidorPessoa) todos[i];
        }
        return resultado;
    }
}