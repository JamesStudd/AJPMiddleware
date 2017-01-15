/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utility;

import java.util.LinkedHashMap;

/**
 * A hashmap implementation that restricts the size of the members
 *
 * @param <T> - The key type
 * @param <J> - The Value type
 */
public class BoundedHashMap<T, J> {

	LinkedHashMap<T, J> map = new LinkedHashMap<T, J>();
	private int size = 0;
	private int bound = 0;

	/**
	 * Constructor sets the map max size
	 *
	 * @param bound
	 */
	public BoundedHashMap(int bound) {
		this.bound = bound;
	}

        public int getSize() {
            return size;
        }
        
        

	//If we go over the size the oldest key/value is removed to make room and the value is returned
	/**
	 * This method inserts an entry into the map. If we are going over the
	 * bounded size then we remove the oldest entry in the map and pass that
	 * back to the caller
	 *
	 * @param key - The key of the entry to be put in the map
	 * @param value - The value of the entry to be added to the map
	 * @return - If are going over the max size then the oldest entry is
	 * removed from the map and returned
	 */
	public J putAndRetrieveLostValue(T key, J value) {

		if (bound == 0) {
			return value;
		}
		if (size < bound) {
			map.put(key, value);
			size++;
		} else {
			T first = (T) map.keySet().toArray()[0];
			J lostValue = map.get(first);
			map.remove(first);
			map.put(key, value);
			return lostValue;
		}
		return null;
	}

	/**
	 * Gets the value in the map that matches the key passed in
	 *
	 * @param key - The key that we need the value for
	 * @return - The value that matches the key (null if not there)
	 */
	public J get(T key) {
		if (contains(key)) {
			J ret = map.get(key);
			map.remove(key);
                        size--;
			return ret;
		}
		return null;
	}

	/**
	 * Checks if the passed key is found in the map
	 *
	 * @param key - The key to be checked
	 * @return - True if the key is contained within the map
	 */
	public boolean contains(T key) {
		return map.containsKey(key);
	}

	/**
	 * Removes an entry from the map based on whether the passed key
	 *
	 * @param key - The key where the value is to be removed
	 */
	public void remove(T key) {
		if (map.containsKey(key)) {
			map.remove(key);
			size--;
		}
	}

}
