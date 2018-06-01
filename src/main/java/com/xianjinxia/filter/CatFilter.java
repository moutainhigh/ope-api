package com.xianjinxia.filter;

import com.xianjinxia.logcenter.http.filter.CatHttpFilter;

import javax.servlet.annotation.WebFilter;

/**
 * Created by dengwenjie on 2017/9/14.
 */
@WebFilter(filterName="LogCenterFilter",urlPatterns="/*")
public class CatFilter extends CatHttpFilter {
}
