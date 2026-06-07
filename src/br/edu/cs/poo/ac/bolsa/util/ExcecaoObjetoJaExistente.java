package br.edu.cs.poo.ac.bolsa.util;

public class ExcecaoObjetoJaExistente extends Exception {
    public ExcecaoObjetoJaExistente() {
        super("Objeto já existente");
    }
}