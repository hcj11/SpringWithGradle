package Mvc;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import lombok.extern.apachecommons.CommonsLog;

import java.util.Date;

/**
 * ����������
 * @author Jiaju Zhuang
 **/
@Data
public class DownloadData {
    @ExcelProperty("�ַ�������")
    private String string;
    @ExcelProperty("���ڱ���")
    private Date date;
    @ExcelProperty("���ֱ���")
    private Double doubleData;
}
