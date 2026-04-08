package br.edu.cs.poo.ac.bolsa.dao;

import br.edu.cs.poo.ac.bolsa.entidade.InvestidorEmpresa;

public class DAOInvestidorEmpresa extends DAOGenerico {
    public DAOInvestidorEmpresa() {
        inicializarCadastro(InvestidorEmpresa.class);
    }

    public InvestidorEmpresa buscarInvestidorEmpresa(String cnpj) {
        return (InvestidorEmpresa) cadastro.buscar(cnpj);
    }

    public boolean incluirInvestidorEmpresa(InvestidorEmpresa ie) {
        if (buscarInvestidorEmpresa(ie.getCnpj()) == null) {
            cadastro.incluir(ie, ie.getCnpj());
            return true;
        } else {
            return false;
        }
    }

    public boolean alterarInvestidorEmpresa(InvestidorEmpresa ie) {
        if (buscarInvestidorEmpresa(ie.getCnpj()) != null) {
            cadastro.alterar(ie, ie.getCnpj());
            return true;
        } else {
            return false;
        }
    }

    public boolean excluirInvestidorEmpresa(String cnpj) {
        if (buscarInvestidorEmpresa(cnpj) != null) {
            cadastro.excluir(cnpj);
            return true;
        } else {
            return false;
        }
    }
}