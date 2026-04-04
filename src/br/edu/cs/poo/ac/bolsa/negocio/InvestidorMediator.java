package br.edu.cs.poo.ac.bolsa.negocio;

import java.math.BigDecimal;
import java.time.LocalDate;

import br.edu.cs.poo.ac.bolsa.dao.DAOInvestidorEmpresa;
import br.edu.cs.poo.ac.bolsa.dao.DAOInvestidorPessoa;
import br.edu.cs.poo.ac.bolsa.entidades.Contatos;
import br.edu.cs.poo.ac.bolsa.entidades.Endereco;
import br.edu.cs.poo.ac.bolsa.entidades.FaixaRenda;
import br.edu.cs.poo.ac.bolsa.entidades.InvestidorEmpresa;
import br.edu.cs.poo.ac.bolsa.entidades.InvestidorPessoa;
import br.edu.cs.poo.ac.bolsa.util.MensagensValidacao;
import br.edu.cs.poo.ac.bolsa.util.ValidadorCpfCnpj;

public class InvestidorMediator {

    private DAOInvestidorEmpresa daoInvEmp = new DAOInvestidorEmpresa();
    private DAOInvestidorPessoa daoInvPes = new DAOInvestidorPessoa();

    public InvestidorMediator() {}

    private boolean isNullOrBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    private boolean contemApenasNumeros(String s) {
        return s.matches("[0-9]+");
    }

    private boolean cnpjFormatoValido(String cnpj) {
        if (isNullOrBlank(cnpj)) return false;
        String limpo = cnpj.replaceAll("[^0-9]", "");
        return limpo.length() == 14;
    }

    private boolean cpfFormatoValido(String cpf) {
        if (isNullOrBlank(cpf)) return false;
        String limpo = cpf.replaceAll("[^0-9]", "");
        return limpo.length() == 11;
    }

    private MensagensValidacao validarEndereco(Endereco endereco) {
        MensagensValidacao msgs = new MensagensValidacao();
        if (isNullOrBlank(endereco.getLogradouro())) {
            msgs.adicionar("Logradouro é obrigatório.");
        }
        if (isNullOrBlank(endereco.getNumero())) {
            msgs.adicionar("Número é obrigatório.");
        }
        if (isNullOrBlank(endereco.getPais())) {
            msgs.adicionar("País é obrigatório.");
        }
        if (isNullOrBlank(endereco.getEstado())) {
            msgs.adicionar("Estado é obrigatório.");
        }
        if (isNullOrBlank(endereco.getCidade())) {
            msgs.adicionar("Cidade é obrigatório.");
        }
        return msgs;
    }

    private MensagensValidacao validarContatos(Contatos contatos, boolean ehPessoaJuridica) {
        MensagensValidacao msgs = new MensagensValidacao();

        String email = contatos.getEmail();
        if (isNullOrBlank(email) || !email.contains("@") || !email.contains(".")) {
            msgs.adicionar("E-mail inválido.");
        }

        String fixo = contatos.getTelefoneFixo();
        String celular = contatos.getTelefoneCelular();
        String whats = contatos.getNumeroWhatsApp();

        boolean temFixo = !isNullOrBlank(fixo);
        boolean temCelular = !isNullOrBlank(celular);
        boolean temWhats = !isNullOrBlank(whats);

        if (!temFixo && !temCelular && !temWhats) {
            msgs.adicionar("Pelo menos um telefone deve ser informado.");
        }

        if (temFixo && !contemApenasNumeros(fixo)) {
            msgs.adicionar("Telefone fixo deve conter apenas números.");
        }
        if (temCelular && !contemApenasNumeros(celular)) {
            msgs.adicionar("Telefone celular deve conter apenas números.");
        }
        if (temWhats && !contemApenasNumeros(whats)) {
            msgs.adicionar("Número de WhatsApp deve conter apenas números.");
        }

        if (ehPessoaJuridica && isNullOrBlank(contatos.getNomeParaContato())) {
            msgs.adicionar("Nome para contato é obrigatório para pessoa jurídica.");
        }

        return msgs;
    }

    private MensagensValidacao validar(DadosInvestidor dadosInv) {
        MensagensValidacao msgs = new MensagensValidacao();

        if (dadosInv == null) {
            msgs.adicionar("Dados do investidor devem ser informados.");
            return msgs;
        }

        if (isNullOrBlank(dadosInv.getNome())) {
            msgs.adicionar("Nome é obrigatório.");
        }
        if (dadosInv.getEndereco() == null) {
            msgs.adicionar("Endereço é obrigatório.");
        }
        if (dadosInv.getDataCriacao() == null) {
            msgs.adicionar("Data de criação é obrigatória.");
        }
        if (dadosInv.getContatos() == null) {
            msgs.adicionar("Contatos é obrigatório.");
        }

        if (dadosInv.getEndereco() != null) {
            msgs.adicionar(validarEndereco(dadosInv.getEndereco()));
        }
        if (dadosInv.getContatos() != null) {
            msgs.adicionar(validarContatos(dadosInv.getContatos(), dadosInv.ehInvestidorEmpresa()));
        }

        return msgs;
    }

    private MensagensValidacao validarInvestidorEmpresa(InvestidorEmpresa ie) {
        MensagensValidacao msgs = new MensagensValidacao();
        DadosInvestidor dados = new DadosInvestidor(ie, null);
        msgs.adicionar(validar(dados));

        if (!cnpjFormatoValido(ie.getCnpj())) {
            msgs.adicionar("CNPJ inválido.");
        }
        if (ie.getFaturamento() < 100000.0) {
            msgs.adicionar("Faturamento deve ser maior ou igual a 100000.0.");
        }

        return msgs;
    }

    private MensagensValidacao validarInvestidorPessoa(InvestidorPessoa ip) {
        MensagensValidacao msgs = new MensagensValidacao();
        DadosInvestidor dados = new DadosInvestidor(null, ip);
        msgs.adicionar(validar(dados));

        if (!cpfFormatoValido(ip.getCpf())) {
            msgs.adicionar("CPF inválido.");
        }
        if (ip.getRenda() < 10000.0) {
            msgs.adicionar("Renda deve ser maior ou igual a 10000.0.");
        }

        for (FaixaRenda faixa : FaixaRenda.values()) {
            if (ip.getRenda() >= faixa.getValorInicial() && ip.getRenda() <= faixa.getValorFinal()) {
                ip.setFaixaRenda(faixa);
                break;
            }
        }

        return msgs;
    }

    public MensagensValidacao incluirInvestidorEmpresa(InvestidorEmpresa ie) {
        MensagensValidacao msgs = validarInvestidorEmpresa(ie);
        if (msgs.estaVazio()) {
            if (!daoInvEmp.incluirInvestidorEmpresa(ie)) {
                msgs.adicionar("Investidor Empresa já existente.");
            }
        }
        return msgs;
    }

    public MensagensValidacao alterarInvestidorEmpresa(InvestidorEmpresa ie) {
        MensagensValidacao msgs = validarInvestidorEmpresa(ie);
        if (msgs.estaVazio()) {
            if (!daoInvEmp.alterarInvestidorEmpresa(ie)) {
                msgs.adicionar("Investidor Empresa não existente.");
            }
        }
        return msgs;
    }

    public MensagensValidacao excluirInvestidorEmpresa(String cnpj) {
        MensagensValidacao msgs = new MensagensValidacao();
        if (!cnpjFormatoValido(cnpj)) {
            msgs.adicionar("CNPJ inválido.");
        }
        if (msgs.estaVazio()) {
            if (!daoInvEmp.excluirInvestidorEmpresa(cnpj)) {
                msgs.adicionar("Investidor Empresa não existente.");
            }
        }
        return msgs;
    }

    public InvestidorEmpresa buscarInvestidorEmpresa(String cnpj) {
        if (!cnpjFormatoValido(cnpj)) {
            return null;
        }
        return daoInvEmp.buscarInvestidorEmpresa(cnpj);
    }

    public MensagensValidacao incluirInvestidorPessoa(InvestidorPessoa ip) {
        MensagensValidacao msgs = validarInvestidorPessoa(ip);
        if (msgs.estaVazio()) {
            if (!daoInvPes.incluirInvestidorPessoa(ip)) {
                msgs.adicionar("Investidor Pessoa já existente.");
            }
        }
        return msgs;
    }

    public MensagensValidacao alterarInvestidorPessoa(InvestidorPessoa ip) {
        MensagensValidacao msgs = validarInvestidorPessoa(ip);
        if (msgs.estaVazio()) {
            if (!daoInvPes.alterarInvestidorPessoa(ip)) {
                msgs.adicionar("Investidor Pessoa não existente.");
            }
        }
        return msgs;
    }

    public MensagensValidacao excluirInvestidorPessoa(String cpf) {
        MensagensValidacao msgs = new MensagensValidacao();
        if (!cpfFormatoValido(cpf)) {
            msgs.adicionar("CPF inválido.");
        }
        if (msgs.estaVazio()) {
            if (!daoInvPes.excluirInvestidorPessoa(cpf)) {
                msgs.adicionar("Investidor Pessoa não existente.");
            }
        }
        return msgs;
    }

    public InvestidorPessoa buscarInvestidorPessoa(String cpf) {
        if (!cpfFormatoValido(cpf)) {
            return null;
        }
        return daoInvPes.buscarInvestidorPessoa(cpf);
    }
}