package br.edu.cs.poo.ac.bolsa.util;

import java.io.Serializable;

public abstract class Registro implements Serializable {
    private static final long serialVersionUID = 1L;

    public abstract String getIdentificador();
}
