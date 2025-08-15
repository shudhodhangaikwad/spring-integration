package com.yash.springbootintegration.model;

import java.util.ArrayList;
import java.util.List;

public class FlowStatus {

    private List<String> steps = new ArrayList<>();

    public void addStep(String step) {
        steps.add(step);
    }

    public List<String> getSteps() {
        return steps;
    }
}
