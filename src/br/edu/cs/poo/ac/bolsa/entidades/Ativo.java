package br.edu.cs.poo.ac.bolsa.entidades;

public class Ativo {
    private long codigo;
    private String descricao;
    private double valorMinimoAplicacao;
    private double valorMaximoAplicacao;
    private double taxaMensalMinima;
    private double taxaMensalMaxima;
    private FaixaRenda faixaMinimaPermitida;
    private int prazoEmMeses;

    public Ativo() {}

    public Ativo(long codigo, String descricao, double valorMinimoAplicacao, double valorMaximoAplicacao, double taxaMensalMinima, double taxaMensalMaxima, FaixaRenda faixaMinimaPermitida, int prazoEmMeses) {
        this.codigo = codigo;
        this.descricao = descricao;
        this.valorMinimoAplicacao = valorMinimoAplicacao;
        this.valorMaximoAplicacao = valorMaximoAplicacao;
        this.taxaMensalMinima = taxaMensalMinima;
        this.taxaMensalMaxima = taxaMensalMaxima;
        this.faixaMinimaPermitida = faixaMinimaPermitida;
        this.prazoEmMeses = prazoEmMeses;
    }

    public long getCodigo() { return codigo; }
    public void setCodigo(long codigo) { this.codigo = codigo; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public double getValorMinimoAplicacao() { return valorMinimoAplicacao; }
    public void setValorMinimoAplicacao(double valorMinimoAplicacao) { this.valorMinimoAplicacao = valorMinimoAplicacao; }
    public double getValorMaximoAplicacao() { return valorMaximoAplicacao; }
    public void setValorMaximoAplicacao(double valorMaximoAplicacao) { this.valorMaximoAplicacao = valorMaximoAplicacao; }
    public double getTaxaMensalMinima() { return taxaMensalMinima; }
    public void setTaxaMensalMinima(double taxaMensalMinima) { this.taxaMensalMinima = taxaMensalMinima; }
    public double getTaxaMensalMaxima() { return taxaMensalMaxima; }
    public void setTaxaMensalMaxima(double taxaMensalMaxima) { this.taxaMensalMaxima = taxaMensalMaxima; }
    public FaixaRenda getFaixaMinimaPermitida() { return faixaMinimaPermitida; }
    public void setFaixaMinimaPermitida(FaixaRenda faixaMinimaPermitida) { this.faixaMinimaPermitida = faixaMinimaPermitida; }
    public int getPrazoEmMeses() { return prazoEmMeses; }
    public void setPrazoEmMeses(int prazoEmMeses) { this.prazoEmMeses = prazoEmMeses; }
}