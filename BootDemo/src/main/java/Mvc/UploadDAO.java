package Mvc;

import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * ������������DAO�洢����Ȼ��Ҫ�������spring������Ȼ�㲻����Ҫ�洢��Ҳ����Ҫ����ࡣ
 *
 * @author Jiaju Zhuang
 **/
@Repository
public class UploadDAO {

    public void save(List<UploadData> list) {
        // �����mybatis,������ֱ�ӵ��ö��insert,�Լ�дһ��mapper��������һ������batchInsert,��������һ���Բ���
    }
}
