package org.javafxapp.sae_dev_app_project.classComponent;

public class Parameter {

    private String parameterName;
    private String parameterType;

    public Parameter(String type, String name) {

        parameterType = type;
        parameterName = name;

    }

    public String getName() {
        return parameterName;
    }
    public String getType() {
        return parameterType;
    }

    public String toString() {

        return parameterType + " " + parameterName;

    }

}
