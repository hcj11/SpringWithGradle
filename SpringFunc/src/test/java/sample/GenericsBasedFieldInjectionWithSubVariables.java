package sample;

import context.CompontScan;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.util.List;
import java.util.Map;


@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class GenericsBasedFieldInjectionWithSubVariables extends GenericsBasedFieldInjectionWithVariables<String, Integer> {
}

@Data
class GenericsBasedFieldInjectionWithVariables<S, I> {
    @Autowired
    private S str;
    @Autowired
    private I integer;
    @Autowired
    private List<S> liststrs;
    @Autowired
    private List<I> listints;
    @Autowired
    private CompontScan.Repository<String> stringRepository;
    @Autowired
    private List<CompontScan.Repository<String>> stringRepositorys;
    @Autowired
    private CompontScan.Repository<Integer> integerRepository;
    @Autowired
    private List<CompontScan.Repository<Integer>> integerRepositorys;
    @Autowired
    private Map<String, CompontScan.Repository<Integer>> intRepositoryMap;
    @Autowired
    private Map<String, CompontScan.Repository<String>> stringRepositoryMap;


}
