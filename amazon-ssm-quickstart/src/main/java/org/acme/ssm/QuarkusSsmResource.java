package org.acme.ssm;

import static java.lang.Boolean.TRUE;
import static java.util.stream.Collectors.toMap;

import java.util.Map;
import java.util.stream.Collector;

import javax.annotation.PostConstruct;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import software.amazon.awssdk.services.ssm.model.GetParameterRequest;
import software.amazon.awssdk.services.ssm.model.GetParametersByPathRequest;
import software.amazon.awssdk.services.ssm.model.Parameter;
import software.amazon.awssdk.services.ssm.model.ParameterType;
import software.amazon.awssdk.services.ssm.model.PutParameterRequest;

public abstract class QuarkusSsmResource {

    @ConfigProperty(name = "parameters.path")
    String parametersPath;

    @PostConstruct
    void normalizePath() {
        if (!parametersPath.startsWith("/")) {
            parametersPath = "/" + parametersPath;
        }
        if (!parametersPath.endsWith("/")) {
            parametersPath = parametersPath + "/";
        }
    }

    protected Collector<Parameter, ?, Map<String, String>> parametersToMap() {
        return toMap(p -> p.name().substring(parametersPath.length()), Parameter::value);
    }

    protected GetParametersByPathRequest generateGetParametersByPathRequest() {
        return GetParametersByPathRequest.builder()
                .path(parametersPath)
                .withDecryption(TRUE)
                .build();
    }

    protected PutParameterRequest generatePutParameterRequest(String name, String value, boolean secure) {
        return PutParameterRequest.builder()
                .name(parametersPath + name)
                .value(value)
                .type(secure ? ParameterType.SECURE_STRING : ParameterType.STRING)
                .overwrite(TRUE)
                .build();
    }

    protected GetParameterRequest generateGetParameterRequest(String name) {
        return GetParameterRequest.builder()
                .name(parametersPath + name)
                .withDecryption(TRUE)
                .build();
    }
}