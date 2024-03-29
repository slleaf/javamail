package com.atchihaya.util.emlparserutils.parser;

import java.io.IOException;
import java.io.InputStream;

/**
 * Create by LuoChenXu on 2019/10/16
 */
public class EMLHelper {

    public static EMLContent parser(InputStream in) throws IOException {
        EMLParser parser = new EMLParser();
        parser.parserEML(in);
        return parser.content();
    }
}
