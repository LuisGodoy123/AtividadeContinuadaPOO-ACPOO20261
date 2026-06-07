package br.edu.cs.poo.ac.bolsa.negocio;

import java.math.BigDecimal;

public class DadosTitulo {
	private String cpfOuCnpj;
	private long codigoAtivo;
	private BigDecimal valorInvestido;
	private BigDecimal taxaDiaria;
	
	public DadosTitulo(String cpfOuCnpj, long codigoAtivo, BigDecimal valorInvestido, BigDecimal taxaDiaria) {
		super();
		this.cpfOuCnpj = cpfOuCnpj;
		this.codigoAtivo = codigoAtivo;
		this.valorInvestido = valorInvestido;
		this.taxaDiaria = taxaDiaria;
	}
	public String getCpfOuCnpj() {
		return cpfOuCnpj;
	}
	public void setCpfOuCnpj(String cpfOuCnpj) {
		this.cpfOuCnpj = cpfOuCnpj;
	}
	public long getCodigoAtivo() {
		return codigoAtivo;
	}
	public void setCodigoAtivo(long codigoAtivo) {
		this.codigoAtivo = codigoAtivo;
	}
	public BigDecimal getValorInvestido() {
		return valorInvestido;
	}
	public void setValorInvestido(BigDecimal valorInvestido) {
		this.valorInvestido = valorInvestido;
	}
	public BigDecimal getTaxaDiaria() {
		return taxaDiaria;
	}
	public void setTaxaDiaria(BigDecimal taxaDiaria) {
		this.taxaDiaria = taxaDiaria;
	}
	
}