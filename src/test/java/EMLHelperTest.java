


import com.atchihaya.util.emlparserutils.helper.EMLHelper;
import com.atchihaya.util.emlparserutils.parser.EMLContent;

import com.atchihaya.util.emlparserutils.parser.constants.FieldNameEnum;
import com.atchihaya.util.emlparserutils.parser.content.ApplicationStreamBody;
import jakarta.mail.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import static java.lang.System.in;
import static org.junit.Assert.assertEquals;

/**
 * Create by LuoChenXu on 2019/11/4
 */
public class EMLHelperTest {


    @Before
    public void init() {

    }

    @Test
    public void parserTest() throws IOException {
        InputStream in = null;
        try {
            in = new ClassPathResource("eml.eml").getInputStream();
            EMLContent emlContent=EMLHelper.parser(in);
            System.out.println("From"+"="+emlContent.getHeader(FieldNameEnum.FROM.getName()));
            System.out.println("to"+"="+emlContent.getHeader(FieldNameEnum.TO.getName()));
            System.out.println("subject"+"="+emlContent.getHeader(FieldNameEnum.SUBJECT.getName()));
            System.out.println("data"+"="+emlContent.getHeader(FieldNameEnum.DATE.getName()));
            System.out.println("cc"+"="+emlContent.getHeader(FieldNameEnum.CC.getName()));
            System.out.println("returnpath"+"="+emlContent.getHeader(FieldNameEnum.RETURN_PATH.getName()));
            System.out.println("received"+"="+emlContent.getHeader(FieldNameEnum.RECEIVED.getName()));




        } finally {
            if (in != null) {
                in.close();

            }
        }
    }

    @Test
    public void parserMailBaseInfo() throws IOException {
        InputStream in = null;
        try {
            in = new FileInputStream(getFile("emlfile/eml.eml"));
            EMLContent emlContent = EMLHelper.parser(in);
            assertEquals("1.0", emlContent.getHeader("MIME-Version"));
            assertEquals("1105405146@qq.com", emlContent.getHeader(FieldNameEnum.TO.getName()));
            assertEquals("Alienware · 活动 <Dell_Home@e-chn.dell.com>", emlContent.getHeader(FieldNameEnum.FROM.getName()));
        } finally {
            if (in != null) {
                in.close();
            }
        }
    }

    @After
    public void after() {

    }

    public File getFile(String resourcePath) {
        try {
            File file = new File(EMLHelperTest.class.getResource(resourcePath).toURI());
            return file;
        } catch (URISyntaxException e) {
            throw new IllegalStateException(e);
        }

    }
    @Test
    public void test1() throws MessagingException {
        Properties props = new Properties();
        props.setProperty("mail.store.protocol", "pop3");		// 协议
        props.setProperty("mail.pop3.port", "110");				// 端口
        props.setProperty("mail.pop3.host", "pop3.163.com");	// pop3服务器

        // 创建Session实例对象
        Session session = Session.getInstance(props);
        Store store = session.getStore("pop3");
        store.connect("xyang0917@163.com", "123456abc");

        // 获得收件箱
        Folder folder = store.getFolder("INBOX");
        /* Folder.READ_ONLY：只读权限
         * Folder.READ_WRITE：可读可写（可以修改邮件的状态）
         */
        folder.open(Folder.READ_WRITE);	//打开收件箱

        // 由于POP3协议无法获知邮件的状态,所以getUnreadMessageCount得到的是收件箱的邮件总数
        System.out.println("未读邮件数: " + folder.getUnreadMessageCount());

        // 由于POP3协议无法获知邮件的状态,所以下面得到的结果始终都是为0
        System.out.println("删除邮件数: " + folder.getDeletedMessageCount());
        System.out.println("新邮件: " + folder.getNewMessageCount());

        // 获得收件箱中的邮件总数
        System.out.println("邮件总数: " + folder.getMessageCount());

        // 得到收件箱中的所有邮件,并解析

    }
}
