package com.jarredweb.rest.tools.ui.model;

import java.util.HashMap;

public class PairValue extends HashMap<String, Object> {

    private static final long serialVersionUID = 1L;

    public PairValue() {
        super();
    }

    public PairValue(Object[] pairs) {
        super();
        for (int i = 0; i < pairs.length; i += 2) {
            put((String) pairs[i], pairs[i + 1]);
        }
    }
}
