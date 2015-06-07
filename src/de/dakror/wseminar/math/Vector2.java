/*******************************************************************************
 * Copyright 2015 Maximilian Stark | Dakror <mail@dakror.de>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/


package de.dakror.wseminar.math;

/**
 * @author Dakror
 */
public class Vector2 implements Comparable<Vector2> {
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
	
	public Vector2 zero() {
		scl(0);
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
	
	@Override
	public int compareTo(Vector2 o) {
		if (x == o.x) return Float.compare(y, o.y);
		else return Float.compare(x, o.x);
	}
}
