package org.jeecg.common.http.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.jeecg.common.http.parser.exceptions.ParseException;

/**
 * Author: Myles Megyesi
 */
public interface ContentTypeParser {

    public boolean canParseContentType(String contentType);

    public Map<String, Object> parse(InputStream inputStream, int contentLength) throws ParseException, IOException;
}
