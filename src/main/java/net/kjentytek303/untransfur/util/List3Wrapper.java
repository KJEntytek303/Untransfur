package net.kjentytek303.untransfur.util;

import java.util.ArrayList;
import java.util.List;


public class List3Wrapper<T> {
	public List3Wrapper(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
		list = new ArrayList<>(x*y*z);
	}

	public void set( int x, int y, int z, T elem ) {
		list.set( x * this.y * this.z + y * this.z + z, elem );
	}

	public T get( int x, int y, int z) {
		return list.get( x * this.y * this.z + y * this.z + z );
	}

	public List<T> list;
	public int x;
	public int y;
	public int z;


}
