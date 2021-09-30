package com.paic.domain;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class Fruits {
    private List<String> names;
    private Map<String,String> maps;
}
