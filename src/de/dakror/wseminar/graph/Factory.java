package de.dakror.wseminar.graph;

/**
 * @author Dakror
 */
public interface Factory<T> {
	public T create(int x, int y);
	
	public void set(T t, int x, int y);
}
