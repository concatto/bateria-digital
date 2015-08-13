package br.univali.digidrum;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import jssc.SerialPort;
import jssc.SerialPortException;

public class KeepaliveService extends ScheduledThreadPoolExecutor {
	private static final long INTERVALO_PADRAO = 1000;
	private static final int TOLERANCIA_PADRAO = 3;
	
	private SerialPort porta;
	private boolean ativo = false;
	private long intervalo;
	private int falhas = 0;
	private int tolerancia;
	
	public KeepaliveService(SerialPort porta) {
		this(porta, INTERVALO_PADRAO, TOLERANCIA_PADRAO);
	}
	
	public KeepaliveService(SerialPort porta, long intervalo, int tolerancia) {
		super(1);
		this.porta = porta;
		this.intervalo = intervalo;
		this.tolerancia = tolerancia;
	}
	
	public void receber() {
		falhas = 0;
	}
	
	public void iniciar() {
		if (porta == null || !porta.isOpened()) {
			throw new IllegalStateException("Impossível comunicar com a porta. Confira se está aberta.");
		}
		
		Runnable acao = () -> {
			if (falhas > tolerancia) {
				ativo = false;
				//Explodir
			}
			
			try {
				ativo = porta.writeByte(Protocolo.KEEPALIVE);
			} catch (SerialPortException e) {
				e.printStackTrace();
				ativo = false;
			}
		};
		
		ScheduledFuture<?> future = scheduleAtFixedRate(acao, 0, intervalo, TimeUnit.MILLISECONDS);
		ativo = true;
	}
	
	public boolean isAtivo() {
		return ativo;
	}
}