package uo.ri.util.assertion;

public class StateChecks {
	
	public static void isNotNull(Object obj) {
		isTrue( obj != null, " Cannot be null " );
	}

	public static void isNull(Object obj) {
		isTrue( obj == null, " Must be null " );
	}

	public static void isTrue(boolean test) {
		isTrue( test, "" );
	}
	
	public static void isTrue(boolean test, String msg) {
		if (test == true) return;
		throwException(msg);
	}

	public static void isFalse(boolean test) {
		isTrue( ! test, " Must be false " );
	}

	public static void isFalse(boolean test, String msg) {
		isTrue( ! test, msg );
	}
	
	protected static void throwException(String msg) {
		throw new IllegalStateException( msg );
	}


}
