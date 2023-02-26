package uo.ri.cws.application.util;

import java.util.Optional;

import uo.ri.cws.application.service.BusinessException;
import uo.ri.cws.domain.base.BaseEntity;

public class BusinessChecks {

	public static void hasVersion(BaseEntity e, long version)
			throws BusinessException {
		isTrue(e.getVersion() == version, "Entity with stale data");
	}

	public static void isNull(Object o, String errorMsg)
			throws BusinessException {
		isTrue(o == null, errorMsg);
	}

	public static void isNull(Object o) throws BusinessException {
		isTrue(o == null, o.getClass().getName() + " cannot be null here");
	}

	public static void isNotNull(Object o, String errorMsg)
			throws BusinessException {
		isTrue(o != null, errorMsg);
	}

	public static void isNotNull(Object o) throws BusinessException {
		isTrue(o != null, o.getClass().getName() + " cannot be null here");
	}

	public static void isFalse(boolean condition) throws BusinessException {
		isTrue(!condition, "Invalid assertion");
	}

	public static void isFalse(boolean condition, String errorMsg)
			throws BusinessException {
		isTrue(!condition, errorMsg);
	}

	public static void exists(Optional<?> owo, String msg)
			throws BusinessException {
		isTrue(owo.isPresent(), msg);
	}

	public static void exists(Optional<?> owo) throws BusinessException {
		exists(owo, "There is no such entity");
	}

	public static void isNotEmpty(String str, String msg)
			throws BusinessException {
		isTrue(str != null && !str.isEmpty(), msg);
	}

	public static void isTrue(boolean condition) throws BusinessException {
		isTrue(condition, "Invalid assertion");
	}

	public static void isTrue(boolean condition, String msg)
			throws BusinessException {
		if (condition == false) {
			throw new BusinessException(msg);
		}
	}

}
