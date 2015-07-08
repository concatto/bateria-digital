package br.univali.digidrum;

import java.util.Comparator;

public class InstrumentoComparator implements Comparator<Instrumento> {
	@Override
	public int compare(Instrumento o1, Instrumento o2) {
		return o1.getNome().compareTo(o2.getNome());
	}
}