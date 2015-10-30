package com.tiaonr.ws.security.util;

/**
 * Created by echyong on 10/20/15.
 */
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
public class PasswordEncoderTest {
    public static void main(String[] args) {
        String password = args[0];
        String salt = args[1];
        Md5PasswordEncoder encoder = new Md5PasswordEncoder();
        String encoded = encoder.encodePassword(password, salt);
        System.out.println(password + "{" + salt + "} ===> " + encoded);
    }
}

/* simple test program used to md5 encode existing accounts.
javac -d target/classes -cp target/jobws/WEB-INF/lib/spring-security-core-4.0.2.RELEASE.jar src/main/java/com/tiaonr/ws/security/util/PasswordEncoderTest.java
java -cp target/jobws/WEB-INF/lib/spring-security-core-4.0.2.RELEASE.jar:target/classes com.tiaonr.ws.security.util.PasswordEncoderTest password user
*/

