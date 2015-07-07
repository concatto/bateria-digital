package br.univali.digibat;

public interface BiConsumidor<T, U> {
	public void consumir(T t, U u);
}
