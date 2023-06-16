package uo.ri.util.random;

public class Random {

	public static Integer integer(int min, int max) {
		return (int)(new java.util.Random().nextFloat() * (max - min) + min);
	}

	public static Long longInteger(long min, long max) {
		return (long)(new java.util.Random().nextFloat() * (max - min) + min);
	}

	public static String string(int length) {
		String res = "";
		for(int i = 0; i < length; i++) {
			res += (char) Random.integer('A', 'Z').intValue();
		}
		return res;
	}

	public static String string(char min, char max, int length) {
		String res = "";
		for(int i = 0; i < length; i++) {
			res += (char) Random.integer(min, max+1).intValue();
		}
		return res;
	}

}