package br.univali.digibat;

import java.util.Arrays;

public class ConsumidorBateria implements ByteArrayConsumer {
	@Override
	public void accept(byte[] bytes) {
		System.out.print(Arrays.toString(bytes) + " ");
		if (bytes[0] == 1) System.out.println();
	}
}
