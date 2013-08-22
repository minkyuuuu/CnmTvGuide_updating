package hdlnc.dev2.sangtae.cnm.global.DATA;


import java.util.ArrayList;

import android.util.Log;

public class VOD_Search {

	ArrayList<VOD_INFO> TargetArrayList;
	ArrayList<VOD_INFO> ResultArrayList = new ArrayList<VOD_INFO>();
	String Keyword;
	
	public VOD_Search(ArrayList<VOD_INFO> target, String keyword)
	{
		TargetArrayList = target;
		Keyword = keyword.trim();
		Log.d("VOD_Search", "keyword : "+ keyword);

	}
	
	public ArrayList<VOD_INFO> StartSearch()
	{
		for(VOD_INFO vi : TargetArrayList) 
		{
			Log.d("VOD_Search", "vi.getTitle().indexOf(Keyword) : " + vi.getTitle().indexOf(Keyword));
			Log.d("VOD_Search", "vi.getTitle() : " + vi.getTitle());
			if(vi.getTitle().indexOf(Keyword) > -1)
				ResultArrayList.add(vi);
				
		}
		return ResultArrayList;
	}
}
