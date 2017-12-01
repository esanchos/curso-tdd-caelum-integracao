package br.com.caelum.pm73.dao;

import java.util.Calendar;

import br.com.caelum.pm73.dominio.Lance;
import br.com.caelum.pm73.dominio.Usuario;

public class LanceBuilder {

	private Usuario usuario;
	private double valor;
	private Calendar dataLance;
	
	public LanceBuilder(){
		this.usuario = new Usuario("Jose","jose@gmail.com");
		this.valor = 0.0;
		this.dataLance = Calendar.getInstance();
	}
	
	public LanceBuilder comUsuario(Usuario usuario) {
		this.usuario = usuario;
		return this;
	}
	
	public LanceBuilder comValor(double valor) {
		this.valor = valor;
		return this;
	}
	
	public LanceBuilder comData(Calendar dataLance) {
		this.dataLance = dataLance;
		return this;
	}
	
	public Lance controi() {
		Lance lance = new Lance(dataLance, usuario, valor, null);
		return lance;
	}
}
