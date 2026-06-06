package br.edu.cs.poo.ac.bolsa.util;

public class ComparadorGenerico implements Comparador {

    @Override
    public int comparar(Comparavel comp1, Comparavel comp2) {
        return comp1.comparar(comp2);
    }
}