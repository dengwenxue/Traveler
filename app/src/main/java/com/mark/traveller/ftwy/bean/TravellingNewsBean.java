package com.mark.traveller.ftwy.bean;

import java.util.List;

/**
 * 旅游新闻的javaBean
 * Created by Mark on 2016/11/17 0017.
 */

public class TravellingNewsBean {

    public int error_code;
    public String reason;
    public List<ResultBean> result;

    public class ResultBean {

        public String ctime;
        public String title;
        public String description;
        public String picUrl;
        public String url;
    }
}
