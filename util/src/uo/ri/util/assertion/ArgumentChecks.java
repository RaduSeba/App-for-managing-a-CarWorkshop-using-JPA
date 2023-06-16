package uo.ri.util.assertion;

public abstract class ArgumentChecks {

	public static void isNotNull(final Object obj) {
		isTrue( obj != null, "Cannot be null" );
	}

	public static void isNotNull(final Object obj, String msg) {
		isTrue( obj != null, msg );
	}

	public static void isNull(final Object obj) {
		isTrue( obj == null, "Must be null" );
	}

	public static void isTrue(final boolean test) {
		isTrue(test, "Condition must be true");
	}

	public static void isTrue(final boolean test, final String msg) {
		if (test == true) {
			return;
		}
		throwException(msg);
	}

	public static void isFalse ( final boolean test ) {
		isTrue( ! test );
	}
	
	public static void isFalse ( final boolean test, final String msg ) {
		isTrue( ! test, msg );
	}
	
	public static void isNotBlank(final String str) {
		isNotBlank( str, "The string cannot be null nor empty" );
	}

	public static void isNotBlank(final String str, String msg) {
		isTrue( str != null && ! str.isBlank(), msg );
	}

	public static void isNotEmpty(final String str, String msg) {
		isTrue( str != null && ! str.isEmpty(), msg );
	}

	public static void isNotEmpty(final String str) {
		isNotEmpty(str, "The string cannot be null nor empty");
	}
	
	protected static void throwException(final String msg) {
		throw new IllegalArgumentException( msg );
	}

}
