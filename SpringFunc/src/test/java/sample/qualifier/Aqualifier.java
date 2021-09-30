package sample.qualifier;

import org.springframework.beans.factory.annotation.Qualifier;

import java.lang.annotation.*;


public interface Aqualifier {

}

@SubA
class SubAqualifier implements Aqualifier{}
@SubB
class SubBqualifier implements Aqualifier{}