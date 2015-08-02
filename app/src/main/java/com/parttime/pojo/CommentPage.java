package com.parttime.pojo;

import java.util.List;

/**
 * Created by dehua on 15/8/2.
 */
public class CommentPage {
    public int pageNumber;
    public int pageSize;
    public int totalPage;
    public int totalRow;
    public List<DetailItem> list;


    public static class DetailItem{
        public String title;
        public String remark;
        public String comment;
    }
}
