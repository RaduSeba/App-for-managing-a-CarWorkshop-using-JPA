package uo.ri.util.assertion;


/**
 * A failing assert of this is an indication of a programming error
 * @author alb
 */
public class Assert {

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

	public static void fail(String msg) {
		throwException( msg );
	}

	protected static void throwException(String msg) {
		throw new RuntimeException( msg );
	}

}