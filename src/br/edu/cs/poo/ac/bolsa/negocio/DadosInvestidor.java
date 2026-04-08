package br.edu.cs.poo.ac.bolsa.negocio;

import java.math.BigDecimal;
import java.time.LocalDate;

import br.edu.cs.poo.ac.bolsa.entidade.Contatos;
import br.edu.cs.poo.ac.bolsa.entidade.Endereco;
import br.edu.cs.poo.ac.bolsa.entidade.InvestidorEmpresa;
import br.edu.cs.poo.ac.bolsa.entidade.InvestidorPessoa;

public class DadosInvestidor {
    private String nome;
    private Endereco endereco;
    private LocalDate dataCriacao;
    private BigDecimal bonus;
    private Contatos contatos;
    private boolean ehInvestidorEmpresa;

    public DadosInvestidor() {}

    public DadosInvestidor(InvestidorEmpresa ie, InvestidorPessoa ip) {
        if (ie != null) {
            this.nome = ie.getNome();
            this.endereco = ie.getEndereco();
            this.dataCriacao = ie.getDataAbertura();
            this.bonus = ie.getBonus();
            this.contatos = ie.getContatos();
            this.ehInvestidorEmpresa = true;
        } else if (ip != null) {
            this.nome = ip.getNome();
            this.endereco = ip.getEndereco();
            this.dataCriacao = ip.getDataNascimento();
            this.bonus = ip.getBonus();
            this.contatos = ip.getContatos();
            this.ehInvestidorEmpresa = false;
        }
    }

    public boolean ehInvestidorEmpresa() {
        return ehInvestidorEmpresa;
    }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public Endereco getEndereco() { return endereco; }
    public void setEndereco(Endereco endereco) { this.endereco = endereco; }
    public LocalDate getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDate dataCriacao) { this.dataCriacao = dataCriacao; }
    public BigDecimal getBonus() { return bonus; }
    public void setBonus(BigDecimal bonus) { this.bonus = bonus; }
    public Contatos getContatos() { return contatos; }
    public void setContatos(Contatos contatos) { this.contatos = contatos; }
}