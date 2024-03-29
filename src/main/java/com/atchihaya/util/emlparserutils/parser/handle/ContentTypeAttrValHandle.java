package com.atchihaya.util.emlparserutils.parser.handle;


import com.atchihaya.util.emlparserutils.parser.BodyUtils;
import com.atchihaya.util.emlparserutils.parser.EMLParser;

/**
 * Create by LuoChenXu on 2019/10/15
 */
public class ContentTypeAttrValHandle implements Handle<Character, Character> {
    private final EMLParser parser;

    public ContentTypeAttrValHandle(EMLParser parser) {
        this.parser = parser;
    }


    @Override
    public Character process(Character c) {
        if ('\r' == c || '\n' == c) {
            if (this.parser.lastChar() == '"') {
                this.parser.memory().currentBody().setAttribute(this.parser.currentFieldName(), BodyUtils.translateString(this.parser.current().substring(0, this.parser.current().length() -1)));
            }else{
                this.parser.memory().currentBody().setAttribute(this.parser.currentFieldName(), BodyUtils.translateString(this.parser.current()));
            }
            this.parser.memory().resetCurrent();
            this.parser.selectHandle().contentTypeNameHandle();
        }else if('"' == c && this.parser.current().isEmpty()){

        } else if (';' == c) {
            this.parser.memory().currentBody().setAttribute(this.parser.currentFieldName(), BodyUtils.translateString(this.parser.current()));
            this.parser.memory().resetCurrent();
            this.parser.selectHandle().contentTypeAttrNameHandle();
        }else{
            this.parser.append(c);
        }
        return c;
    }
}
