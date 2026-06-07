package br.edu.cs.poo.ac.bolsa.entidade;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;

import br.edu.cs.poo.ac.bolsa.util.Registro;

public abstract class Investidor extends Registro {

    private String nome;
    private Endereco endereco;
    private LocalDate dataCriacao;
    private BigDecimal bonus = BigDecimal.ZERO;
    private Contatos contatos;

    public Investidor() {}

    public Investidor(String nome, Endereco endereco, LocalDate dataCriacao,
                      BigDecimal bonus, Contatos contatos) {
        this.nome = nome;
        this.endereco = endereco;
        this.dataCriacao = dataCriacao;
        this.bonus = bonus != null ? bonus : BigDecimal.ZERO;
        this.contatos = contatos;
    }

    public abstract BigDecimal getEntradaFinanceira();

    public int getIdade() {
        if (dataCriacao == null) return 0;
        return Period.between(dataCriacao, LocalDate.now()).getYears();
    }

    public void creditarBonus(BigDecimal valor) {
        if (valor != null) {
            this.bonus = this.bonus.add(valor);
        }
    }

    public void debitarBonus(BigDecimal valor) {
        if (valor != null) {
            this.bonus = this.bonus.subtract(valor);
        }
    }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public Endereco getEndereco() { return endereco; }
    public void setEndereco(Endereco endereco) { this.endereco = endereco; }
    protected LocalDate getDataCriacao() { return dataCriacao; }
    protected void setDataCriacao(LocalDate dataCriacao) { this.dataCriacao = dataCriacao; }
    public BigDecimal getBonus() { return bonus; }
    public void setBonus(BigDecimal bonus) { this.bonus = bonus; }
    public Contatos getContatos() { return contatos; }
    public void setContatos(Contatos contatos) { this.contatos = contatos; }
}