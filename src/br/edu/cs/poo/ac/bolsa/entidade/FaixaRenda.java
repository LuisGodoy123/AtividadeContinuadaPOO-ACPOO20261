package br.edu.cs.poo.ac.bolsa.entidade;

public enum FaixaRenda {
    REGULAR(1, "Renda regular", 10000.00, 50000.00),
    DIFERENCIADA(2, "Renda diferenciada", 5000.01, 300000.00),
    PREMIUM(3, "Renda premium", 300000.01, 100000000.00);

    private int codigo;
    private String descricao;
    private double valorInicial;
    private double valorFinal;

    private FaixaRenda(int codigo, String descricao, double valorInicial, double valorFinal) {
        this.codigo = codigo;
        this.descricao = descricao;
        this.valorInicial = valorInicial;
        this.valorFinal = valorFinal;
    }

    public int getCodigo() { return codigo; }
    public String getDescricao() { return descricao; }
    public double getValorInicial() { return valorInicial; }
    public double getValorFinal() { return valorFinal; }
}