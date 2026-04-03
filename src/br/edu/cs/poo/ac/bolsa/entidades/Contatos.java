package br.edu.cs.poo.ac.bolsa.entidades;

public class Contatos {
    private String email;
    private String telefoneFixo;
    private String telefoneCelular;
    private String numeroWhatsApp;
    private String nomeParaContato;

    public Contatos() {}

    public Contatos(String email, String telefoneFixo, String telefoneCelular, String numeroWhatsApp, String nomeParaContato) {
        this.email = email;
        this.telefoneFixo = telefoneFixo;
        this.telefoneCelular = telefoneCelular;
        this.numeroWhatsApp = numeroWhatsApp;
        this.nomeParaContato = nomeParaContato;
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getTelefoneFixo() { return telefoneFixo; }
    public void setTelefoneFixo(String telefoneFixo) { this.telefoneFixo = telefoneFixo; }
    public String getTelefoneCelular() { return telefoneCelular; }
    public void setTelefoneCelular(String telefoneCelular) { this.telefoneCelular = telefoneCelular; }
    public String getNumeroWhatsApp() { return numeroWhatsApp; }
    public void setNumeroWhatsApp(String numeroWhatsApp) { this.numeroWhatsApp = numeroWhatsApp; }
    public String getNomeParaContato() { return nomeParaContato; }
    public void setNomeParaContato(String nomeParaContato) { this.nomeParaContato = nomeParaContato; }
}