package com.parttime.main;

import java.util.Comparator;

import com.quark.model.HuanxinUser;

/**
*
* @ClassName: PinyinComparator
* @Description: 圈子 根据名字拼音排序 a-z排序
* @author howe
* @date 2015-2-11 下午5:37:19
*
*/
public class PinyinComparator implements Comparator<HuanxinUser> {

	public int compare(HuanxinUser o1, HuanxinUser o2) {
        if(o1 == null || o1.getNamePinyin() == null){
            return 1;
        }
        if(o2 == null || o2.getNamePinyin() == null){
            return 0;
        }
		if (o1.getNamePinyin().equals("@")
				|| o2.getNamePinyin().equals("#")) {
			return -1;
		} else if (o1.getNamePinyin().equals("#")
				|| o2.getNamePinyin().equals("@")) {
			return 1;
		} else {
			return o1.getNamePinyin().compareTo(o2.getNamePinyin());
		}
	}

}
