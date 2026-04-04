package br.edu.cs.poo.ac.bolsa.util;

public enum ResultadoValidacao {
	
	FORMATO_INVALIDO(1, "Formato inválido"),
	DV_INVALIDO(2, "Dígito verificador inválido"),
	NAO_INFORMADO(3, "CFP ou CNPJ não informado");
	
	private int codigo;
	private String mensagem;
	
	private ResultadoValidacao(int codigo, String mensagem) {
		this.codigo = codigo;
		this.mensagem = mensagem;
	}
	public int getCodigo() {
		return codigo;
	}
	public String getMensagem() {
		return mensagem;
	}	
}