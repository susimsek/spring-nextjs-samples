package io.github.susimsek.springnextjssamples.logging.formatter;

import io.github.susimsek.springnextjssamples.logging.model.HttpLog;
import io.github.susimsek.springnextjssamples.logging.model.MethodLog;

public interface LogFormatter {
    String format(HttpLog httpLog);

    String format(MethodLog methodLog);
}
