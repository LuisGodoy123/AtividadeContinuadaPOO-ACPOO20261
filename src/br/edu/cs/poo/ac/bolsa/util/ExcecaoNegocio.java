package br.edu.cs.poo.ac.bolsa.util;

public class ExcecaoNegocio extends Exception {
	private MensagensValidacao mensagens;
	public ExcecaoNegocio(MensagensValidacao mensagens) {
		super();
		this.mensagens = mensagens;
	}
	public MensagensValidacao getMensagens() {
		return mensagens;
	} 
}