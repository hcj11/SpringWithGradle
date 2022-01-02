package mybatis;

import org.junit.jupiter.api.Test;

public class MybatisCacheTest extends Mybatis{
    /**
     * second cache has the problem of the dirty read.
     * in the distribution sys， can use the central cache ie: read ,memcache ,..
     */
    @Test
    public void cacheTest(){

    }
}
