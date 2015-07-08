package br.univali.digidrum;

import java.util.Comparator;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;

@SuppressWarnings("serial")
class SortedComboBoxModel<E> extends DefaultComboBoxModel<E> {
	private Comparator<E> comparator;

	public SortedComboBoxModel() {
		super();
	}

	public SortedComboBoxModel(Comparator<E> comparator) {
		super();
		this.comparator = comparator;
	}

	public SortedComboBoxModel(E items[]) {
		this(items, null);
	}

	public SortedComboBoxModel(E items[], Comparator<E> comparator) {
		this.comparator = comparator;

		for (E item : items) {
			addElement(item);
		}
	}

	public SortedComboBoxModel(Vector<E> items) {
		this(items, null);
	}

	public SortedComboBoxModel(Vector<E> items, Comparator<E> comparator) {
		this.comparator = comparator;

		for (E item : items) {
			addElement(item);
		}
	}

	@Override
	public void addElement(E element) {
		insertElementAt(element, 0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void insertElementAt(E element, int index) {
		int size = getSize();

		for (index = 0; index < size; index++) {
			if (comparator != null) {
				E o = getElementAt(index);

				if (comparator.compare(o, element) > 0)	break;
			} else {
				Comparable<E> c = (Comparable<E>) getElementAt(index);

				if (c.compareTo(element) > 0) break;
			}
		}

		super.insertElementAt(element, index);

		if (index == 0 && element != null) {
			setSelectedItem(element);
		}
	}
}