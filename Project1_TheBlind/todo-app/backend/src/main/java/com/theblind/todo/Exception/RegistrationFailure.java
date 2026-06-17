package com.theblind.todo.Exception;

import java.util.Map;
import java.util.ArrayList;
import java.util.List;

public class RegistrationFailure extends RuntimeException {
    /**
    * RegistrationFailure - a custom exception to be thrown when user registration fails
    *
    * @param generic_message - a string containing a generic message describing the error
    * @param invalidRequirementsList - a list containing key-value pairs. The key is a
    *                                  string representing the name of the requirement, 
    *                                  and the value is a boolean representing whether the 
    *                                  requirement was satisfied (true = valid, false = invalid)
    */
    public RegistrationFailure(String generic_message, List<Map.Entry<String, Boolean>> invalidRequirementsList) {
        super(generic_message);
    }
}
