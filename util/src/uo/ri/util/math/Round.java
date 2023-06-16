package uo.ri.util.math;

public class Round {

	public static double twoCents(double importe) {
		return (double) Math.round( importe * 100) / 100;
	}

}
