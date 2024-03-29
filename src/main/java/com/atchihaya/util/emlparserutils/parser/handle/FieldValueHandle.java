package com.atchihaya.util.emlparserutils.parser.handle;


import com.atchihaya.util.emlparserutils.parser.BodyUtils;
import com.atchihaya.util.emlparserutils.parser.EMLParser;

/**
 * Create by LuoChenXu on 2019/10/10
 * 域值处理器
 */
public class FieldValueHandle implements Handle<Character, Character>{

    // 解析器
    private final EMLParser parser;

    public FieldValueHandle(EMLParser parser) {
        this.parser = parser;
    }

    @Override
    public Character process(Character c) {
        if ('\r' == c || '\n' == c) {
            this.parser.content().addHeader(this.parser.currentFieldName(), BodyUtils.translateString(this.parser.current()).trim());
            this.parser.selectHandle().unknowHandle();
        } else {
            this.parser.append(c);
        }
        return c;
    }
}
