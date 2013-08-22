package hdlnc.dev2.sangtae.cnm.global.DATA;

import java.util.ArrayList;
import java.util.HashMap;

public class VOD_GENRE_HASHMAP extends HashMap<String, ArrayList<VOD_GENRE>> implements Cloneable {

	private ArrayList<String> Keys = new ArrayList<String>();

	@Override
	public Object clone() {
		// TODO Auto-generated method stub
		return (VOD_GENRE_HASHMAP)super.clone();
	}

	public void addKey(String key) {
		Keys.add(key);
	}
	
	public ArrayList<String> getKeys() {
		return Keys;
	}

}
