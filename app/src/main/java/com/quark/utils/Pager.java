package com.quark.utils;

/**
 * Created by Administrator on 11/4 0004.
 * 分页辅助类，记录当前查询结果数
 */
public class Pager {

    private static final int DEFAUL_PAGE_CAPACITY = 20;
    //每页查询的条数，也就是这里页的大小
    private int pageCapacity;

    //结果总页数
    private int totalPage = -1;

    //当前页
    private int mCurrentPage =0 ;

    //总记录条数
    private int totalNum;

    public Pager() {
        pageCapacity = DEFAUL_PAGE_CAPACITY;
    }

    public Pager(int capacity) {
        pageCapacity = capacity;
    }

    public void setPageCapacity(int pageCapacity){
        this.pageCapacity = pageCapacity;
    }

    public void updateTotalNum(int totalNum){
        this.totalNum = totalNum;
    }

    public void updateTotalPager(int totalPage){
        this.totalPage = totalPage;
    }

    public boolean isLastPage() {
        if (mCurrentPage == totalPage) {
            return true;
        }
        return false;
    }

    public int nextPager(){
        if(!isLastPage()){
            mCurrentPage++;
        }
        return mCurrentPage;
    }


    public int getPageCapacity() {
        return pageCapacity;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public int getmCurrentPage() {
        return mCurrentPage;
    }

    public int getTotalNum() {
        return totalNum;
    }

    public void reset(){
        mCurrentPage = -1;
        totalPage = -1;
        totalNum = -1;
        pageCapacity = DEFAUL_PAGE_CAPACITY;
    }
}
