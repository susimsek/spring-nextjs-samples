package io.github.susimsek.springnextjssamples.config.logging.model;

import io.github.susimsek.springnextjssamples.config.logging.enums.MethodLogType;
import io.github.susimsek.springnextjssamples.config.tracing.Trace;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MethodLog {
    private MethodLogType type;
    private String className;
    private String methodName;
    private Object[] arguments;
    private Object result;
    private String exceptionMessage;
    private Long durationMs;
    private Trace trace;
}
