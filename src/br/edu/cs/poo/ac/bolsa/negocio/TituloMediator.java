package br.edu.cs.poo.ac.bolsa.negocio;

import java.math.BigDecimal;
import java.time.LocalDate;

import br.edu.cs.poo.ac.bolsa.dao.DAO;
import br.edu.cs.poo.ac.bolsa.entidade.Ativo;
import br.edu.cs.poo.ac.bolsa.entidade.Investidor;
import br.edu.cs.poo.ac.bolsa.entidade.StatusTitulo;
import br.edu.cs.poo.ac.bolsa.entidade.Titulo;
import br.edu.cs.poo.ac.bolsa.util.ExcecaoNegocio;
import br.edu.cs.poo.ac.bolsa.util.ExcecaoObjetoJaExistente;
import br.edu.cs.poo.ac.bolsa.util.MensagensValidacao;

public class TituloMediator {

    private static TituloMediator instancia;

    private DAO<Titulo> daoTitulo = new DAO<>(Titulo.class);
    private AtivoMediator ativoMediator = AtivoMediator.getInstancia();
    private InvestidorMediator investidorMediator = new InvestidorMediator();

    private TituloMediator() {}

    public static TituloMediator getInstancia() {
        if (instancia == null) {
            instancia = new TituloMediator();
        }
        return instancia;
    }

    public void incluir(DadosTitulo dados) throws ExcecaoNegocio {
        MensagensValidacao msgs = new MensagensValidacao();

        if (dados.getCpfOuCnpj() == null || dados.getCpfOuCnpj().trim().isEmpty()) {
            msgs.adicionar("CPF/CNPJ inválido");
        }
        if (dados.getCodigoAtivo() <= 0) {
            msgs.adicionar("Código do ativo inválido");
        }
        if (dados.getValorInvestido() == null) {
            msgs.adicionar("Valor investido não pode ser nulo");
        }
        if (dados.getTaxaDiaria() == null) {
            msgs.adicionar("Taxa diária não pode ser nula");
        }

        if (!msgs.estaVazio()) throw new ExcecaoNegocio(msgs);

        Ativo ativo = ativoMediator.buscar(dados.getCodigoAtivo());
        if (ativo == null) {
            msgs.adicionar("Ativo não encontrado");
            throw new ExcecaoNegocio(msgs);
        }

        Investidor investidor = investidorMediator.buscarInvestidor(dados.getCpfOuCnpj());
        if (investidor == null) {
            msgs.adicionar("Investidor não encontrado");
            throw new ExcecaoNegocio(msgs);
        }

        if (dados.getValorInvestido().compareTo(BigDecimal.valueOf(ativo.getValorMinimoAplicacao())) < 0
                || dados.getValorInvestido().compareTo(BigDecimal.valueOf(ativo.getValorMaximoAplicacao())) > 0) {
            msgs.adicionar("Valor investido fora da faixa permitida");
            throw new ExcecaoNegocio(msgs);
        }

        double taxaDiariaDbl = dados.getTaxaDiaria().doubleValue() / 100.0;
        double taxaMensal = 100 * (Math.pow(1 + taxaDiariaDbl, 30) - 1);
        if (taxaMensal < ativo.getTaxaMensalMinima() || taxaMensal > ativo.getTaxaMensalMaxima()) {
            msgs.adicionar("Taxa diária fora da faixa permitida");
            throw new ExcecaoNegocio(msgs);
        }

        if (investidor.getEntradaFinanceira().compareTo(
                BigDecimal.valueOf(ativo.getFaixaMinimaPermitida().getValorInicial())) < 0) {
            msgs.adicionar("Entrada financeira do investidor abaixo da faixa exigida");
            throw new ExcecaoNegocio(msgs);
        }

        LocalDate hoje = LocalDate.now();
        Titulo titulo = new Titulo(
                investidor, ativo,
                dados.getValorInvestido(), dados.getValorInvestido(),
                dados.getTaxaDiaria(),
                hoje,
                hoje.plusMonths(ativo.getPrazoEmMeses()),
                null,
                StatusTitulo.ATIVO
        );

        try {
            daoTitulo.incluir(titulo);
        } catch (ExcecaoObjetoJaExistente e) {
            msgs.adicionar("Título já existente");
            throw new ExcecaoNegocio(msgs);
        }
    }

    public void processarRendimentos() {
        Titulo[] titulos = daoTitulo.buscarTodos();
        if (titulos == null) return;

        for (Titulo titulo : titulos) {
            boolean rendeu = titulo.render();

            if (rendeu) {
                BigDecimal bonus = titulo.getValorAtual()
                        .subtract(titulo.getValorInvestido())
                        .multiply(new BigDecimal("0.0001"));

                Investidor investidor = investidorMediator
                        .buscarInvestidor(titulo.getInvestidor().getIdentificador());
                if (investidor != null) {
                    investidor.creditarBonus(bonus);
                    investidorMediator.alterarInvestidor(investidor);
                }
            }

            if (!titulo.getDataVencimento().isAfter(LocalDate.now())) {
                titulo.setStatus(StatusTitulo.VENCIDO);
            }

            try {
                daoTitulo.alterar(titulo);
            } catch (Exception e) {
                // ignorar
            }
        }
    }

    public void cancelarTitulo(String numero) throws ExcecaoNegocio {
        MensagensValidacao msgs = new MensagensValidacao();

        Titulo titulo = daoTitulo.buscar(numero);
        if (titulo == null) {
            msgs.adicionar("Título não encontrado");
            throw new ExcecaoNegocio(msgs);
        }

        if (titulo.getStatus() == StatusTitulo.VENCIDO
                || titulo.getStatus() == StatusTitulo.CANCELADO) {
            msgs.adicionar("Título não pode ser cancelado");
            throw new ExcecaoNegocio(msgs);
        }

        titulo.setStatus(StatusTitulo.CANCELADO);

        Investidor investidor = investidorMediator
                .buscarInvestidor(titulo.getInvestidor().getIdentificador());
        if (investidor != null) {
            BigDecimal debito = investidor.getBonus()
                    .multiply(new BigDecimal("0.70"));
            investidor.debitarBonus(debito);
            investidorMediator.alterarInvestidor(investidor);
        }

        try {
            daoTitulo.alterar(titulo);
        } catch (Exception e) {
            // ignorar
        }
    }
}