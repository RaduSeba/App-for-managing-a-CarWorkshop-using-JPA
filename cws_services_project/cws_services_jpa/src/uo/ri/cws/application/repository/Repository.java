package uo.ri.cws.application.repository;

import java.util.List;
import java.util.Optional;

/**
 *	Common interface for a repository that follows the Collection metaphor,
 *	 in the same way as any Collection in Java (List, Set, etc). You only put 
 *	 and remove objects (references indeed), thus there is no update method. 
 *	 That is an important difference with the TDG/DAO pattern.
 *
 * @param <T>
 */
public interface Repository<T> {
	void add(T t);
	void remove(T t);
	Optional<T> findById(String id);
	List<T> findAll();
}

