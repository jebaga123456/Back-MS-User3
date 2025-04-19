package com.exp.cl.utils;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;


@Component
@RequiredArgsConstructor
public class PBKDF2Encoder implements PasswordEncoder{

	private final com.exp.cl.config.JwtProperties jwtProperties;

	@Override
	public String encode(CharSequence cs) {
		try {
			byte[] result = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512")
											.generateSecret(new PBEKeySpec(cs.toString().toCharArray(), jwtProperties.getSecret().getBytes(), jwtProperties.getIteration(), jwtProperties.getKeylength()))
											.getEncoded();
			return Base64.getEncoder().encodeToString(result);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
			throw new RuntimeException(ex);
		}
	}

	@Override
	public boolean matches(CharSequence cs, String string) {
		return encode(cs).equals(string);
	}
	
}
