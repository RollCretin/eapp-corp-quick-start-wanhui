/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: ExcelData
 * Author:   cretin
 * Date:     12/26/18 09:02
 * Description: excel导出model
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.model.excel;

import java.io.Serializable;
import java.util.List;

/**
 * 〈excel导出model〉
 *
 * @author cretin
 * @create 12/26/18
 * @since 1.0.0
 */
public class ExcelData implements Serializable {

    private static final long serialVersionUID = 4444017239100620999L;

    // 表头
    private List<String> titles;

    // 数据
    private List<List<Object>> rows;

    // 页签名称
    private String name;

    public List<String> getTitles() {
        return titles;
    }

    public void setTitles(List<String> titles) {
        this.titles = titles;
    }

    public List<List<Object>> getRows() {
        return rows;
    }

    public void setRows(List<List<Object>> rows) {
        this.rows = rows;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}