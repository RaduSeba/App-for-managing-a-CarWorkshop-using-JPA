package uo.ri.cws.infrastructure.persistence.jpa.util;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;

public class BaseJpaRepository<T> {
	
	public void add(T t) {
		Jpa.getManager().persist( t );
	}

	public void remove(T t) {
		Jpa.getManager().remove( t );
	}

	public Optional<T> findById(String id) {
		T found = Jpa.getManager().find(type, id);
		return Optional.ofNullable( found );
	}

	public List<T> findAll() {
		String entity = type.getName();
		String query = "select o from " + entity + " o";
		
		return Jpa.getManager()
				.createQuery(query, type)
				.getResultList();
	}

	/**
	 * As find() and the query "select x from X x" needs the type of the entity
	 * here there is a reflective way of getting it
	 */
	private Class<T> type;

	public BaseJpaRepository() {
		this.type = hackTheTypeOfGenericParameter();
	 }

	/**
	 * This is a hack to recover the runtime reflective type of <T>
	 */
	@SuppressWarnings("unchecked")
	private Class<T> hackTheTypeOfGenericParameter() {
		ParameterizedType superType = 
			(ParameterizedType)	getClass().getGenericSuperclass();
	    return (Class<T>) superType.getActualTypeArguments()[0];
	}
	
}
