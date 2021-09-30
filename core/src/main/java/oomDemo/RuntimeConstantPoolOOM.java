package oomDemo;

import java.util.ArrayList;
import java.util.List;

/**
 * VM Args��-XX:PermSize=10M -XX:MaxPermSize=10M
 * @author zzm
 * Java HotSpot(TM) 64-Bit Server VM warning: ignoring option PermSize=10M; support was removed in 8.0
 * Java HotSpot(TM) 64-Bit Server VM warning: ignoring option MaxPermSize=10M; support was removed in 8.0
 */
public class RuntimeConstantPoolOOM {

	public static void main(String[] args) {
		// ʹ��List�����ų��������ã�����Full GC���ճ�������Ϊ
		List<String> list = new ArrayList<String>();
		// 10MB��PermSize��integer��Χ���㹻����OOM��
		int i = 0; 
		while (true) {
			list.add(String.valueOf(i++).intern());
		}
	}
}