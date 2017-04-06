package com.vidmt.lmei.util.think;

import java.util.Comparator;

import com.vidmt.lmei.entity.Persion;

/**
 * 
 * @author xiaanming
 *
 */
public class PinyinComparator implements Comparator<Persion> {

	public int compare(Persion o1, Persion o2) {
		if (o1.getEnglishSorting().equals("@")
				|| o2.getEnglishSorting().equals("#")) {
			return -1;
		} else if (o1.getEnglishSorting().equals("#")
				|| o2.getEnglishSorting().equals("@")) {
			return 1;
		} else {
			return o1.getEnglishSorting().compareTo(o2.getEnglishSorting());
		}
	}

}
