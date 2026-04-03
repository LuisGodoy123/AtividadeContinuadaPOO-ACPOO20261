package br.edu.cs.poo.ac.bolsa.entidades;

import java.math.BigDecimal;
import java.time.LocalDate;

public class InvestidorPessoa extends Investidor {
    private String cpf;
    private double renda;
    private FaixaRenda faixaRenda;

    public InvestidorPessoa() {}

    public InvestidorPessoa(String nome, Endereco endereco, LocalDate dataNascimento, BigDecimal bonus, Contatos contatos, String cpf, double renda, FaixaRenda faixaRenda) {
        super(nome, endereco, dataNascimento, bonus, contatos);
        this.cpf = cpf;
        this.renda = renda;
        this.faixaRenda = faixaRenda;
    }

    public LocalDate getDataNascimento() { return getDataCriacao(); }
    public void setDataNascimento(LocalDate dataNascimento) { setDataCriacao(dataNascimento); }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public double getRenda() { return renda; }
    public void setRenda(double renda) { this.renda = renda; }
    public FaixaRenda getFaixaRenda() { return faixaRenda; }
    public void setFaixaRenda(FaixaRenda faixaRenda) { this.faixaRenda = faixaRenda; }
}