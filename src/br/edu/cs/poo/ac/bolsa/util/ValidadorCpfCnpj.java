package br.edu.cs.poo.ac.bolsa.util;

public class ValidadorCpfCnpj {

    public static ResultadoValidacao validarCpf(String cpf) {
        if (cpf == null || cpf.trim().isEmpty()) {
            return ResultadoValidacao.NAO_INFORMADO;
        }

        cpf = cpf.replaceAll("[^0-9]", "");

        if (cpf.length() != 11 || cpf.matches("(\\d)\\1{10}")) {
            return ResultadoValidacao.FORMATO_INVALIDO;
        }

        int soma = 0;
        for (int i = 0; i < 9; i++) {
            soma += (cpf.charAt(i) - '0') * (10 - i);
        }
        int d1 = 11 - (soma % 11);
        if (d1 >= 10) d1 = 0;
        if (d1 != (cpf.charAt(9) - '0')) {
            return ResultadoValidacao.DV_INVALIDO;
        }

        soma = 0;
        for (int i = 0; i < 10; i++) {
            soma += (cpf.charAt(i) - '0') * (11 - i);
        }
        int d2 = 11 - (soma % 11);
        if (d2 >= 10) d2 = 0;
        if (d2 != (cpf.charAt(10) - '0')) {
            return ResultadoValidacao.DV_INVALIDO;
        }

        return null;
    }

    public static ResultadoValidacao validarCnpj(String cnpj) {
        if (cnpj == null || cnpj.trim().isEmpty()) {
            return ResultadoValidacao.NAO_INFORMADO;
        }

        cnpj = cnpj.replaceAll("[^0-9]", "");

        if (cnpj.length() != 14 || cnpj.matches("(\\d)\\1{13}")) {
            return ResultadoValidacao.FORMATO_INVALIDO;
        }

        int[] pesos1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        int soma = 0;
        for (int i = 0; i < 12; i++) {
            soma += (cnpj.charAt(i) - '0') * pesos1[i];
        }
        int d1 = 11 - (soma % 11);
        if (d1 >= 10) d1 = 0;
        if (d1 != (cnpj.charAt(12) - '0')) {
            return ResultadoValidacao.DV_INVALIDO;
        }

        int[] pesos2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        soma = 0;
        for (int i = 0; i < 13; i++) {
            soma += (cnpj.charAt(i) - '0') * pesos2[i];
        }
        int d2 = 11 - (soma % 11);
        if (d2 >= 10) d2 = 0;
        if (d2 != (cnpj.charAt(13) - '0')) {
            return ResultadoValidacao.DV_INVALIDO;
        }

        return null;
    }
}