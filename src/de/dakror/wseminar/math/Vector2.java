package de.dakror.wseminar.math;

/**
 * @author Dakror
 */
public class Vector2 {
	public float x, y;
	
	public Vector2() {}
	
	public Vector2(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector2 add(Vector2 o) {
		add(o.x, o.y);
		
		return this;
	}
	
	public Vector2 add(float x, float y) {
		this.x += x;
		this.y += y;
		
		return this;
	}
	
	public Vector2 sub(Vector2 o) {
		add(-o.x, -o.y);
		
		return this;
	}
	
	public Vector2 sub(float x, float y) {
		add(-x, -y);
		
		return this;
	}
	
	public Vector2 set(float x, float y) {
		this.x = x;
		this.y = y;
		
		return this;
	}
	
	public Vector2 set(Vector2 v) {
		set(v.x, v.y);
		
		return this;
	}
	
	public float length() {
		return (float) Math.sqrt(x * x + y * y);
	}
	
	public Vector2 nor() {
		float length = length();
		x /= length;
		y /= length;
		
		return this;
	}
	
	/**
	 * @return a new normalized instance, the old stays unmodified.
	 */
	public Vector2 normalize() {
		return clone().nor();
	}
	
	public Vector2 scl(float skalar) {
		mul(skalar, skalar);
		
		return this;
	}
	
	public Vector2 mul(float scX, float scY) {
		x *= scX;
		y *= scY;
		
		return this;
	}
	
	@Override
	public Vector2 clone() {
		return new Vector2(x, y);
	}
	
	public float dst(Vector2 o) {
		float dx = x - o.x;
		float dy = y - o.y;
		
		return (float) Math.sqrt(dx * dx + dy * dy);
	}
	
	@Override
	public String toString() {
		return "[" + x + ", " + y + "]";
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof Vector2 ? x == ((Vector2) obj).x && y == ((Vector2) obj).y : false;
	}
}
