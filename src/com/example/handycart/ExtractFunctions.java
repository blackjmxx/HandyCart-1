package com.example.handycart;

import java.util.HashMap;

import static com.example.handycart.Constant.FIRST_COLUMN;
import static com.example.handycart.Constant.SECOND_COLUMN;
import static com.example.handycart.Constant.THIRD_COLUMN;

public class ExtractFunctions {

public ExtractFunctions(){
	
}

	public HashMap getArticle(String[] part)
	{
		HashMap item = new HashMap();
		item.put(FIRST_COLUMN,part[0]);
        item.put(SECOND_COLUMN,part[1]);
        item.put(THIRD_COLUMN,part[2]);
		return item;
	}
}
