package com.parttime.publish.vo;

import java.util.List;

/**
 * 已发布活动列表查询结果VO
 * Created by wyw on 2015/7/28.
 */
public class PublishActivityListVo {
    public int pageNumber;
    public int pageSize;
    public int totlePage;
    public int totleRow;
    public List<JobManageListVo> jobManageListVoList;
}
