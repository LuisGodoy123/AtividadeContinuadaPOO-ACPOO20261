package br.edu.cs.poo.ac.bolsa.util;

import java.util.ArrayList;
import java.util.List;

public class MensagensValidacao {
	private List<String> mensagens = new ArrayList<String>();
	public void adicionar(String mensagem) {
		mensagens.add(mensagem);
	}
	public void adicionar(MensagensValidacao lista) {
		mensagens.addAll(lista.mensagens);
	}
	public boolean estaVazio() {
		return mensagens.isEmpty();
	}
	public String[] getMensagens() {
		String[] arr = new String[mensagens.size()];
		int i = 0;
		for (String string : mensagens) {
			arr[i] = string;
			i++;
		}
		return arr;
	}
}