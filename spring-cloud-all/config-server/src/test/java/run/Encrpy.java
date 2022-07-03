package run;

import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.cloud.config.server.encryption.CipherEnvironmentEncryptor;
import org.springframework.cloud.context.encrypt.EncryptorFactory;
import org.springframework.security.crypto.encrypt.TextEncryptor;

import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;

@Data

public class Encrpy {
    /**
     *
     */
    private CipherEnvironmentEncryptor encryptor;
    public TextEncryptor textEncryptor;

    public Encrpy() {
        this.encryptor = new CipherEnvironmentEncryptor(
                keys -> textEncryptor);
    }

    @DisplayName("properties:")
    @ParameterizedTest(name = "{index}:{0},{1}")
    @MethodSource(value = "valueRange")
    public void params(String salt, String key) {
        textEncryptor = new EncryptorFactory().create(salt);
        String encrypt = textEncryptor.encrypt(key);
        String decrypt = textEncryptor.decrypt(encrypt);
        Assertions.assertEquals(key, decrypt);

    }


    static Stream valueRange() {
        return Stream.of(arguments("deadbeef", "foo"), arguments("BBQ", "bar"));
    }

    @Test
    public void encrpytor() {
        String salt = "deadbeef";
        String key = "foo";
        String encrypt = textEncryptor.encrypt(key);
        String decrypt = textEncryptor.decrypt(encrypt);
        Assertions.assertEquals(key, decrypt);
    }
}
