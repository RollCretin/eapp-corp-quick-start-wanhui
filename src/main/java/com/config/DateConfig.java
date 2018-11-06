package com.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

@Component
public class DateConfig {

    private static Resource dateRes;

    @Value( "classpath:static/data/date.json" )
    public void setDateRes(Resource dateRes) {
        DateConfig.dateRes = dateRes;
    }

    private static List<DateBean> dates;

    /**
     * 获取对应年月的上班日期
     *
     * @param yearAndMonth
     * @return
     */
    public static DateBean getDates(String yearAndMonth) {
        if ( dates == null || dates.isEmpty() ) {
            try {
                String json = IOUtils.toString(dateRes.getInputStream(), Charset.forName("UTF-8"));
                ObjectMapper mapper = new ObjectMapper();
                dates = mapper.readValue(json, new TypeReference<List<DateBean>>() {
                });
            } catch ( IOException e ) {
                e.printStackTrace();
            }
        }
        DateBean dateBean = new DateBean();
        dateBean.setYear(yearAndMonth);
        if ( dates.contains(dateBean) ) {
            return dates.get(dates.indexOf(dateBean));
        }
        return null;
    }
}
