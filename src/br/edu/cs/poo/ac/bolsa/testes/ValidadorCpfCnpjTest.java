package br.edu.cs.poo.ac.bolsa.testes;

import org.junit.jupiter.api.Test;

import br.edu.cs.poo.ac.bolsa.util.ResultadoValidacao;
import br.edu.cs.poo.ac.bolsa.util.ValidadorCpfCnpj;

import static org.junit.jupiter.api.Assertions.*;

public class ValidadorCpfCnpjTest {

    // ---------------------------------------------------------
    //  TESTES CPF
    // ---------------------------------------------------------

    @Test
    public void testCpfNaoInformado() {
        assertEquals(ResultadoValidacao.NAO_INFORMADO,
                ValidadorCpfCnpj.validarCpf(null));

        assertEquals(ResultadoValidacao.NAO_INFORMADO,
                ValidadorCpfCnpj.validarCpf(""));
    }

    @Test
    public void testCpfFormatoInvalido() {
        assertEquals(ResultadoValidacao.FORMATO_INVALIDO,
                ValidadorCpfCnpj.validarCpf("123")); // muito curto

        assertEquals(ResultadoValidacao.FORMATO_INVALIDO,
                ValidadorCpfCnpj.validarCpf("111.111.111-11")); // repetido
    }

    @Test
    public void testCpfDvInvalido() {
        assertEquals(ResultadoValidacao.DV_INVALIDO,
                ValidadorCpfCnpj.validarCpf("12345678908")); // DV errado
    }

    @Test
    public void testCpfValido() {
        // CPF válido conhecido
        assertNull(ValidadorCpfCnpj.validarCpf("529.982.247-25"));
    }
    
    @Test
    public void testCpfValido1() {
        // CPF válido conhecido
        assertNull(ValidadorCpfCnpj.validarCpf("52998224725"));
    }

    // ---------------------------------------------------------
    //  TESTES CNPJ
    // ---------------------------------------------------------

    @Test
    public void testCnpjNaoInformado() {
        assertEquals(ResultadoValidacao.NAO_INFORMADO,
                ValidadorCpfCnpj.validarCnpj(null));

        assertEquals(ResultadoValidacao.NAO_INFORMADO,
                ValidadorCpfCnpj.validarCnpj(""));
    }

    @Test
    public void testCnpjFormatoInvalido() {
        assertEquals(ResultadoValidacao.FORMATO_INVALIDO,
                ValidadorCpfCnpj.validarCnpj("123")); // muito curto

        assertEquals(ResultadoValidacao.FORMATO_INVALIDO,
                ValidadorCpfCnpj.validarCnpj("11.111.111/1111-11")); // repetido
    }

    @Test
    public void testCnpjDvInvalido() {
        assertEquals(ResultadoValidacao.DV_INVALIDO,
                ValidadorCpfCnpj.validarCnpj("12.345.678/0001-00"));
    }

    @Test
    public void testCnpjValido() {
        // CNPJ válido conhecido
        assertNull(ValidadorCpfCnpj.validarCnpj("45.723.174/0001-10"));
    }
    @Test
    public void testCnpjValido1() {
        // CNPJ válido conhecido
        assertNull(ValidadorCpfCnpj.validarCnpj("45723174000110"));
    }
}
