package com.config;

import org.joda.time.DateTime;

import java.util.Collections;
import java.util.List;

/**
 * @date: on 10/25/18
 * @author: cretin
 * @email: mxnzp_life@163.com
 * @desc: 添加描述
 */
public class DateBean {

    /**
     * year : 201801
     * month : [{"day":"02"},{"day":"03"},{"day":"04"},{"day":"05"},{"day":"08"},{"day":"09"},{"day":"10"},{"day":"11"},{"day":"12"},{"day":"15"},{"day":"16"},{"day":"17"},{"day":"18"},{"day":"19"},{"day":"22"},{"day":"23"},{"day":"24"},{"day":"25"},{"day":"26"},{"day":"29"},{"day":"30"},{"day":"31"}]
     */

    private String year;
    private List<MonthBean> month;

    @Override
    public boolean equals(Object obj) {
        DateBean o = ( DateBean ) obj;
        return getYear().equals(o.getYear());
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public List<MonthBean> getMonth() {
        return month;
    }

    public void setMonth(List<MonthBean> month) {
        this.month = month;
    }

    public static class MonthBean implements Comparable<MonthBean> {
        /**
         * day : 02
         */

        private String day;

        public MonthBean() {
        }

        public MonthBean(String day) {
            this.day = day;
        }

        public String getDay() {
            return day;
        }

        @Override
        public boolean equals(Object obj) {
            MonthBean bean = ( MonthBean ) obj;
            return Integer.parseInt(bean.getDay()) == Integer.parseInt(getDay());
        }

        public void setDay(String day) {
            this.day = day;
        }

        @Override
        public int compareTo(MonthBean o) {
            try {
                int oldDay = Integer.parseInt(day);
                int day = Integer.parseInt(o.getDay());
                return oldDay - day;
            } catch ( Exception e ) {
                return 1;
            }
        }
    }

    /**
     * 获取之前天数之前的上班天数
     *
     * @param today
     * @return
     */
    public int getDaysBefore(int today) {
        if ( month != null && !month.isEmpty() ) {
            //对month排序
            Collections.sort(month);
            try {
                for ( MonthBean monthBean : month ) {
                    if ( Integer.parseInt(monthBean.day) == today ) {
                        return month.indexOf(monthBean);
                    } else if ( Integer.parseInt(monthBean.day) > today ) {
                        if ( month.contains(new MonthBean(today + "")) ) {
                            return month.indexOf(monthBean) - 1;
                        } else {
                            return month.indexOf(monthBean);
                        }
                    }
                }
            } catch ( Exception e ) {
                return -1;
            }
        }
        return -1;
    }

    //到today一共上了多少天班
    public int getDays(int today) {
        if ( month != null && !month.isEmpty() ) {
            //对month排序
            Collections.sort(month);
            try {
                for ( MonthBean monthBean : month ) {
                    if ( Integer.parseInt(monthBean.day) == today ) {
                        return month.indexOf(monthBean) + 1;
                    } else if ( Integer.parseInt(monthBean.day) > today ) {
                        return month.indexOf(monthBean);
                    }
                }
            } catch ( Exception e ) {
                return -1;
            }
        }
        return -1;
    }

    /**
     * 检查今日是否是工作日
     *
     * @return
     */
    public boolean isTodayInWork() {
        DateTime now = DateTime.now();
        if ( month.contains(new MonthBean(now.getDayOfMonth() + "")) ) {
            return true;
        }
        return false;
    }
}
