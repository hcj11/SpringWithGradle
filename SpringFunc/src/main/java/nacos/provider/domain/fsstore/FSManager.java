package nacos.provider.domain.fsstore;

import com.paic.domain.fsstore.FSEntity;

public interface FSManager {
    FSEntity getObject(String var1, String var2);

    FSEntity putObject(String var1, FSEntity var2);

    boolean deleteObject(String var1, String var2);
}