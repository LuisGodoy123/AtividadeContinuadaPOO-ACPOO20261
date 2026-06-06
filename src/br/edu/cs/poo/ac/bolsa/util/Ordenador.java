package br.edu.cs.poo.ac.bolsa.util;

public class Ordenador {

    public static void ordenar(Comparavel[] comps) {
        if (comps == null) return;
        ordenar(comps, new ComparadorGenerico());
    }

    public static void ordenar(Comparavel[] comps, Comparador comparador) {
        if (comps == null || comparador == null) return;
        for (int i = 0; i < comps.length - 1; i++) {
            for (int j = 0; j < comps.length - 1 - i; j++) {
                if (comparador.comparar(comps[j], comps[j + 1]) > 0) {
                    Comparavel temp = comps[j];
                    comps[j] = comps[j + 1];
                    comps[j + 1] = temp;
                }
            }
        }
    }
}