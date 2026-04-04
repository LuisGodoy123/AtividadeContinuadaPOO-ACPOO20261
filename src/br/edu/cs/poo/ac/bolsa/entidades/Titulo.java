package br.edu.cs.poo.ac.bolsa.entidades;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.io.Serializable;

public class Titulo implements Serializable {
    private InvestidorPessoa investidorPessoa;
    private InvestidorEmpresa investidorEmpresa;
    private Ativo ativo;
    private BigDecimal valorInvestido;
    private BigDecimal valorAtual;
    private BigDecimal taxaDiaria;
    private LocalDate dataAplicacao;
    private LocalDate dataVencimento;
    private LocalDate dataUltimoRendimento;
    private StatusTitulo status;

    public Titulo() {}

    public Titulo(InvestidorPessoa investidorPessoa, InvestidorEmpresa investidorEmpresa, Ativo ativo, BigDecimal valorInvestido, BigDecimal valorAtual, BigDecimal taxaDiaria, LocalDate dataAplicacao, LocalDate dataVencimento, LocalDate dataUltimoRendimento, StatusTitulo status) {
        this.investidorPessoa = investidorPessoa;
        this.investidorEmpresa = investidorEmpresa;
        this.ativo = ativo;
        this.valorInvestido = valorInvestido;
        this.valorAtual = valorAtual;
        this.taxaDiaria = taxaDiaria;
        this.dataAplicacao = dataAplicacao;
        this.dataVencimento = dataVencimento;
        this.dataUltimoRendimento = dataUltimoRendimento;
        this.status = status;
    }

    public boolean render() {
        LocalDate hoje = LocalDate.now();

        // Se o status do titulo for diferente de ativo, nao atualizar o valor atual
        if (status != StatusTitulo.ATIVO) return false;
        // Se a data atual for maior ou igual a data de vencimento, nao atualizar
        if (!hoje.isBefore(dataVencimento)) return false;
        // Se a data atual for menor ou igual a data de aplicacao, nao atualizar
        if (!hoje.isAfter(dataAplicacao)) return false;
        // Se a data atual for menor ou igual a data de ultimo rendimento, nao atualizar
        if (dataUltimoRendimento != null && !hoje.isAfter(dataUltimoRendimento)) return false;

        LocalDate dataBase;
        // Se a data de ultimo rendimento for null, calcular diferenca entre data atual e data da aplicacao
        if (dataUltimoRendimento == null) {
            dataBase = dataAplicacao;
        } else {
            // Se a data de ultimo rendimento for diferente de null, calcular diferenca entre data atual e data de ultimo rendimento
            dataBase = dataUltimoRendimento;
        }

        long dias = ChronoUnit.DAYS.between(dataBase, hoje);
        if (dias <= 0) return false;

        // Formula: valorAtual = valorAtual * (1 + taxaDiaria/100) ^ dias
        BigDecimal taxaDecimal = taxaDiaria.divide(new BigDecimal("100"));
        BigDecimal fator = BigDecimal.ONE.add(taxaDecimal).pow((int) dias);
        this.valorAtual = this.valorAtual.multiply(fator);
        this.dataUltimoRendimento = hoje;

        return true;
    }

    public String getNumero() {
        String identificador;
        // Se investidorPessoa for diferente de null: "000" + cpf + codigo do ativo + YYYYMMDD
        if (investidorPessoa != null) {
            identificador = "000" + investidorPessoa.getCpf();
        } else {
            // Se investidorEmpresa for diferente de null: cnpj + codigo do ativo + YYYYMMDD
            identificador = investidorEmpresa.getCnpj();
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String dataFormatada = dataAplicacao.format(formatter) + "0000";

        return identificador + ativo.getCodigo() + dataFormatada;
    }

    // Getters e Setters
    public InvestidorPessoa getInvestidorPessoa() { return investidorPessoa; }
    public void setInvestidorPessoa(InvestidorPessoa investidorPessoa) { this.investidorPessoa = investidorPessoa; }
    public InvestidorEmpresa getInvestidorEmpresa() { return investidorEmpresa; }
    public void setInvestidorEmpresa(InvestidorEmpresa investidorEmpresa) { this.investidorEmpresa = investidorEmpresa; }
    public Ativo getAtivo() { return ativo; }
    public void setAtivo(Ativo ativo) { this.ativo = ativo; }
    public BigDecimal getValorInvestido() { return valorInvestido; }
    public void setValorInvestido(BigDecimal valorInvestido) { this.valorInvestido = valorInvestido; }
    public BigDecimal getValorAtual() { return valorAtual; }
    public void setValorAtual(BigDecimal valorAtual) { this.valorAtual = valorAtual; }
    public BigDecimal getTaxaDiaria() { return taxaDiaria; }
    public void setTaxaDiaria(BigDecimal taxaDiaria) { this.taxaDiaria = taxaDiaria; }
    public LocalDate getDataAplicacao() { return dataAplicacao; }
    public void setDataAplicacao(LocalDate dataAplicacao) { this.dataAplicacao = dataAplicacao; }
    public LocalDate getDataVencimento() { return dataVencimento; }
    public void setDataVencimento(LocalDate dataVencimento) { this.dataVencimento = dataVencimento; }
    public LocalDate getDataUltimoRendimento() { return dataUltimoRendimento; }
    public void setDataUltimoRendimento(LocalDate dataUltimoRendimento) { this.dataUltimoRendimento = dataUltimoRendimento; }
    public StatusTitulo getStatus() { return status; }
    public void setStatus(StatusTitulo status) { this.status = status; }
}