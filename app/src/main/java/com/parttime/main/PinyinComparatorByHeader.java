package com.parttime.main;

import java.util.Comparator;

import com.easemob.chatuidemo.domain.User;

/**
*
* @ClassName: PinyinComparator
* @Description: 圈子 根据名字拼音排序 a-z排序 排序环信的数据
* @author howe
* @date 2015-2-11 下午5:37:19
*
*/
public class PinyinComparatorByHeader implements Comparator<User> {

	public int compare(User o1, User o2) {
		if (o1.getHeader().equals("@")
				|| o2.getHeader().equals("#")) {
			return -1;
		} else if (o1.getHeader().equals("#")
				|| o2.getHeader().equals("@")) {
			return 1;
		} else {
			return o1.getHeader().compareTo(o2.getHeader());
		}
	}

}
