/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utility;

import java.util.LinkedHashMap;

/**
 *
 * @author chris
 */


//A boundedhash map, with a max capacity
public class BoundedHashMap <T, J> {

	LinkedHashMap<T, J> map = new LinkedHashMap<T, J>();
	private int size = 0;
	private int bound = 0;

	public BoundedHashMap(int bound) {
		this.bound = bound;
	}


	//If we go over the size the oldest key/value is removed to make room and the value is returned
	public J putAndRetrieveLostValue(T key, J value){

		if(size < bound){
			map.put(key, value);
			size++;
		}
		else{
			T first = (T) map.keySet().toArray()[0];
			J lostValue = map.get(first);
			map.remove(first);
			map.put(key, value);
			return lostValue;
		}
		return null;
	}

	//Gets the value at the passed key
	public J get(T key){
		if(contains(key)){
			J ret = map.get(key);
			map.remove(key);
			return ret;
		}
		return null;
	}

	public boolean contains(T key){
		return map.containsKey(key);
	}

	//Removes the key value pair at the passed key
	public void remove(T key){
		map.remove(key);
		size--;
	}

	
	
}
