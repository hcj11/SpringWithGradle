package Mvc;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import lombok.extern.apachecommons.CommonsLog;

import java.util.Date;

/**
 * 基础数据类
 * @author Jiaju Zhuang
 **/
@Data
public class DownloadData {
    @ExcelProperty("字符串标题")
    private String string;
    @ExcelProperty("日期标题")
    private Date date;
    @ExcelProperty("数字标题")
    private Double doubleData;
}
