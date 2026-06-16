package com.theblind.todo.Exception;

import java.util.Map;
import java.util.ArrayList;
import java.util.List;

public class RegistrationFailure extends RuntimeException {
    public RegistrationFailure(String message, List<Map.Entry<String, Boolean>> invalidRequirementsList) {
        super(message);
    }
}
