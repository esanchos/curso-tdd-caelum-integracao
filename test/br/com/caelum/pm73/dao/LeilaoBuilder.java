package br.com.caelum.pm73.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.com.caelum.pm73.dominio.Lance;
import br.com.caelum.pm73.dominio.Leilao;
import br.com.caelum.pm73.dominio.Usuario;

public class LeilaoBuilder {

	private Usuario dono;
	private double valor;
	private String nome;
	private boolean usado;
	private Calendar dataAbertura;
	private boolean encerrado;
	private List<Lance> lances;
	
	public LeilaoBuilder(){
		this.dono = new Usuario("Jose","jose@gmail.com");
		this.valor = 1500.0;
		this.nome = "TV";
		this.usado = false;
		this.dataAbertura = Calendar.getInstance();
		this.lances = new ArrayList();
	}
	
	public LeilaoBuilder comDono(Usuario dono) {
		this.dono = dono;
		return this;
	}
	
	public LeilaoBuilder comValor(double valor) {
		this.valor = valor;
		return this;
	}
	
	public LeilaoBuilder comNome(String nome) {
		this.nome = nome;
		return this;
	}
	
	public LeilaoBuilder usado() {
		this.usado = true;
		return this;
	}
	
	public LeilaoBuilder encerrado() {
		this.encerrado = true;
		return this;
	}
	
	public LeilaoBuilder diasAtras(int dias) {
		Calendar data = Calendar.getInstance();
		data.add(Calendar.DAY_OF_MONTH, -dias);
		this.dataAbertura = data;
		return this;
	}
	
	public LeilaoBuilder naData(Calendar calendar) {
		this.dataAbertura = calendar;
		return this;
	}
	
	public LeilaoBuilder comLance(Lance lance) {
		this.lances.add(lance);
		return this;
	}
	
	public Leilao controi() {
		Leilao leilao = new Leilao(nome, valor, dono, usado);
		leilao.setDataAbertura(dataAbertura);
		if (encerrado) leilao.encerra();
		for (Lance lance : lances) {
			leilao.adicionaLance(lance);
		}
		return leilao;
	}
}
