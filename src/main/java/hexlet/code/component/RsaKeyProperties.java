package hexlet.code.component;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties(prefix = "rsa")
@Setter
@Getter
public class RsaKeyProperties {
    private RSAPublicKey publicKey;
    private RSAPrivateKey privateKey;

//    /**
//     * void init.
//     */
//    @PostConstruct
//    public void init() {
//        System.out.println("Public key: " + publicKey);
//        System.out.println("Private key " + privateKey);
//    }
}
