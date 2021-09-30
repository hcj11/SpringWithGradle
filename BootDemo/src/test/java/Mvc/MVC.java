package Mvc;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.ResultMatcher.matchAll;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@Slf4j
public class MVC {
    @Test
    public void webGet() throws Exception {
        MockMvc build = standaloneSetup(WebController.class)
              .addFilter(new CustomFilter(),new String[]{"/web/*"}).build();
        String s = (String) build.perform(MockMvcRequestBuilders.get("/web/get")).andReturn().getResponse().getContentAsString();
        log.info("{}",s);
    }
    @Test
    public void post() throws Exception {
        MockMvc build = standaloneSetup(Controller.class).build();
        build.perform(MockMvcRequestBuilders.post("/post").
                header("attr","1").content("{\"name\":\"123\"}").contentType(MediaType.APPLICATION_JSON))
        .andExpect(result -> {
            log.info("{}",result.getResponse().getContentAsString());
        });
    }
    @Test
    public void insertParam() throws Exception {
        MockMvc build = standaloneSetup(Controller.class).build();
        build.perform(MockMvcRequestBuilders.post("/insertParam").
                contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE).
                param("hello", "w").param("1", "2")
        );
    }

    @Test
    public void insert() throws Exception {
        MockMvc build = standaloneSetup(Controller.class).build();
        build.perform(MockMvcRequestBuilders.post("/insert").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"string\":null,\n" +
                        "    \"age\":1,\n" +
                        "    \"hellos\":[\"w\",\"o\",\"r\",\"l\",\"d\"],\n" +
                        "    \"phone\":\"183333\"\n" +
                        "}"))
                .andExpect(result -> {
                    log.info("insert: {}", result.getResponse().getContentAsString());
                });
    }

    @Test
    public void test() throws Exception {
        MockMvc build = standaloneSetup(Controller.class).build();
        MockHttpServletRequestBuilder callable = MockMvcRequestBuilders.get("/get")
                .characterEncoding(StandardCharsets.UTF_8.name());

        build.perform(callable).andExpect(matchAll(result -> {
            content().json("{\"msg\":\"msg: helloworld\",\"status\":\"0\"}");
            log.info("get: {}", result.getResponse().getContentAsString());
        }));

        String contentAsString = (String) build.perform(MockMvcRequestBuilders.get("/callable"))
                .andExpect(matchAll(
                        status().isOk()
                )).andReturn().getAsyncResult(20000);
        log.info("callable: {}", contentAsString);


    }

    @Test
    public void demo1() {
        A mock = mock(A.class);
        B b = new B(mock);
        b.SayHello();


        verify(mock, times(1)).SayHello();
        verifyNoMoreInteractions(mock);
        verify(mock, atMost(1)).SayHello();
        ;
        verify(mock, atLeast(1)).SayHello();
        ;
        verify(mock, atLeastOnce()).SayHello();
        ;
        inOrder().verify(mock, calls(1)).SayHello();
    }
}
