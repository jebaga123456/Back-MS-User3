package com.exp.cl.utils;

import java.io.IOException;
import java.io.Serializable;
import java.security.interfaces.RSAKey;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JWTUtils implements Serializable {

	private static final long serialVersionUID = 1L;
	
	
    public String getPrivateKeyPath() throws IOException {
    ClassPathResource privateKeyResource = new ClassPathResource("pems/rsa-private.pem");
    return privateKeyResource.getFile().getAbsolutePath();
    }

    public String getPublicKeyPath() throws IOException {
        ClassPathResource privateKeyResource = new ClassPathResource("pems/rsa-public.pem");
        return privateKeyResource.getFile().getAbsolutePath();
    }
	public Boolean validateToken(String token) {
        RSAKey key;
        if (token != null) {
            try {            	
                key = (RSAKey) PemUtils.readPublicKeyFromFile(getPublicKeyPath(), "RSA");
                DecodedJWT jwt = JWT.require( Algorithm. RSA256(key))
                        //.withSubject(subject)
                        .build()
                        .verify(token);
                return true;
            }catch (JWTDecodeException e){
                System.out.println(e.getMessage());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;

    }
    
	public Map<String,Claim> getAllClaimsToken(String token) {
        RSAKey key;
        Map<String,Claim> claims= new HashMap<String, Claim>();
        if (token != null) {
            
            try {
                key = (RSAKey) PemUtils.readPublicKeyFromFile(getPublicKeyPath(), "RSA");
                DecodedJWT jwt = JWT.require( Algorithm. RSA256(key))
                        //.withSubject(subject)
                        .build()
                        .verify(token);
                claims =jwt.getClaims();
                //claims.
                return claims;
            }catch (JWTDecodeException e){
                System.out.println(e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
		return claims;
    }
		
	
	public String getUsernameFromToken(String token) {
        RSAKey key;
        if (token != null) {
            String user=null;
            try {
                key = (RSAKey) PemUtils.readPublicKeyFromFile(getPublicKeyPath(), "RSA");
                DecodedJWT jwt = JWT.require( Algorithm. RSA256(key))
                        //.withSubject(subject)
                        .build()
                        .verify(token);
                user =jwt.getSubject();
                return user;
            }catch (JWTDecodeException e){
                System.out.println(e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }
	
 public Authentication getAuthentication(String token) {        
        String email = getUsernameFromToken(token);
        return new UsernamePasswordAuthenticationToken(email, null, Collections.emptyList());
    }
        
	public String generateToken(String userName , String email) {				
		return doGenerateToken(userName, email);
	}

	private String doGenerateToken(String username, String email) {

		RSAKey key;		
		final Date createdDate = new Date();
		final Date expirationDate = new Date(createdDate.getTime()+new Long(900000000));		
		 try {
			key = (RSAKey) PemUtils.readPrivateKeyFromFile(getPrivateKeyPath(), "RSA");
			 Algorithm algorithm = Algorithm.RSA256(key);
	            return JWT.create()
	                    .withIssuer("oath")
	                    .withIssuedAt(createdDate)
	                    .withExpiresAt(expirationDate)
	                    .withSubject(email)	                    
	                    .sign(algorithm);
			
		} catch (IOException e) {
			e.printStackTrace();
			return null;
			// TODO Auto-generated catch block
			
		}
		 
		
	}
}