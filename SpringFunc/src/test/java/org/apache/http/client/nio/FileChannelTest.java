package org.apache.http.client.nio;

import lombok.extern.slf4j.Slf4j;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;

import static org.easymock.EasyMock.*;

@Slf4j
public class FileChannelTest {

    @Test
    public void reportTest() throws SQLException {


        DataSource dataSource = (DataSource) mock(DataSource.class);
        expect(dataSource.getLoginTimeout()).andReturn(1);
        EasyMock.replay(dataSource);
        dataSource.getLoginTimeout();
        EasyMock.verify(dataSource);
    }
    @Test
    public void easyMockTest() throws SQLException {
        Connection connection = (Connection) mock(Connection.class);
        DataSource dataSource = (DataSource) mock(DataSource.class);
        expect(dataSource.getConnection()).andAnswer(()->{
            log.info("mock be invoke1!!!");
            return connection;
        });
        expect(dataSource.getConnection()).andAnswer(()->{
            log.info("mock be invoke2!!!");
            return connection;
        });

        EasyMock.replay(dataSource);
        dataSource.getConnection();
        dataSource.getConnection();
        EasyMock.verify(dataSource);


    }
    @Test
    public void readAndThenSend(){
        File file = Paths.get("").toFile();
        MultipartFile multipartFile  = null;
        try {
            multipartFile.transferTo(file);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
