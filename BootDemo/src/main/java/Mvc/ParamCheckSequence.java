package Mvc;

import javax.validation.GroupSequence;
import javax.validation.groups.Default;

interface First{}

interface Second{}

@GroupSequence({Default.class,First.class,Second.class})
public interface ParamCheckSequence {

}
