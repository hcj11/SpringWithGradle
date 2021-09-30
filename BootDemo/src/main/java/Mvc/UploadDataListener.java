package Mvc;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * ģ��Ķ�ȡ��
 *
 * @author Jiaju Zhuang
 */
// �и�����Ҫ�ĵ� DemoDataListener ���ܱ�spring����Ҫÿ�ζ�ȡexcel��Ҫnew,Ȼ�������õ�spring���Թ��췽������ȥ
public class UploadDataListener extends AnalysisEventListener<UploadData> {
    private static final Logger LOGGER =
        LoggerFactory.getLogger(UploadDataListener.class);
    /**
     * ÿ��5���洢���ݿ⣬ʵ��ʹ���п���3000����Ȼ������list �������ڴ����
     */
    private static final int BATCH_COUNT = 5;
    List<UploadData> list = new ArrayList<UploadData>();
    /**
     * ���������һ��DAO����Ȼ��ҵ���߼����Ҳ������һ��service����Ȼ������ô洢�������û�á�
     */
    private UploadDAO uploadDAO;

    public UploadDataListener() {
        // ������demo���������newһ����ʵ��ʹ���������spring,��ʹ��������вι��캯��
        uploadDAO = new UploadDAO();
    }

    /**
     * ���ʹ����spring,��ʹ��������췽����ÿ�δ���Listener��ʱ����Ҫ��spring������ഫ����
     *
     * @param uploadDAO
     */
    public UploadDataListener(UploadDAO uploadDAO) {
        this.uploadDAO = uploadDAO;
    }

    /**
     * ���ÿһ�����ݽ�������������
     *
     * @param data
     *            one row value. Is is same as {@link AnalysisContext#readRowHolder()}
     * @param context
     */
    @Override
    public void invoke(UploadData data, AnalysisContext context) {
        LOGGER.info("������һ������:{}", JSON.toJSONString(data));
        list.add(data);
        // �ﵽBATCH_COUNT�ˣ���Ҫȥ�洢һ�����ݿ⣬��ֹ���ݼ������������ڴ棬����OOM
        if (list.size() >= BATCH_COUNT) {
            saveData();
            // �洢������� list
            list.clear();
        }
    }

    /**
     * �������ݽ�������� ����������
     *
     * @param context
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // ����ҲҪ�������ݣ�ȷ���������������Ҳ�洢�����ݿ�
        saveData();
        LOGGER.info("�������ݽ�����ɣ�");
    }

    /**
     * ���ϴ洢���ݿ�
     */
    private void saveData() {
        LOGGER.info("{}�����ݣ���ʼ�洢���ݿ⣡", list.size());
        uploadDAO.save(list);
        LOGGER.info("�洢���ݿ�ɹ���");
    }
}
