package io.github.susimsek.springnextjssamples.config.logging.formatter;

import io.github.susimsek.springnextjssamples.config.logging.model.HttpLog;
import io.github.susimsek.springnextjssamples.config.logging.model.MethodLog;

public interface LogFormatter {
    String format(HttpLog httpLog);

    String format(MethodLog methodLog);
}
