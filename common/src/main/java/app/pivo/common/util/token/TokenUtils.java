//package app.pivo.common.util.token;
//
//import org.eclipse.microprofile.jwt.Claims;
//import org.jose4j.jws.AlgorithmIdentifiers;
//import org.jose4j.jws.JsonWebSignature;
//import org.jose4j.jwt.JwtClaims;
//import org.jose4j.jwt.NumericDate;
//
//import java.io.InputStream;
//import java.nio.charset.StandardCharsets;
//import java.security.KeyFactory;
//import java.security.PrivateKey;
//import java.security.spec.PKCS8EncodedKeySpec;
//import java.util.Base64;
//
//public class TokenUtils {
//
//    private TokenUtils() {}
//
//    public static String generateToken(JwtClaims claims) throws Exception {
//        PrivateKey pk = readPrivateKey();
//
//        long currentTimeInSecs = currentTimeInSecs();
//
//        claims.setIssuedAt(NumericDate.fromSeconds(currentTimeInSecs));
//        claims.setClaim(Claims.auth_time.name(), NumericDate.fromSeconds(currentTimeInSecs));
//
//        JsonWebSignature jws = new JsonWebSignature();
//        jws.setPayload(claims.toJson());
//        jws.setKey(pk);
//        jws.setHeader("typ", "JWT");
//        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);
//
//        return jws.getCompactSerialization();
//    }
//
//    private static PrivateKey readPrivateKey() throws Exception {
//        InputStream is = TokenUtils.class.getResourceAsStream("/keys/private.pem");
//        byte[] temp = new byte[4096];
//        int length = is.read(temp);
//        return decodePrivateKey(new String(temp, 0, length, StandardCharsets.UTF_8));
//    }
//
//    /**
//     * Decode a PEM encoded private key string to an RSA PrivateKey
//     *
//     * @param pemEncoded - PEM string for private key
//     * @return PrivateKey
//     * @throws Exception on decode failure
//     */
//    private static PrivateKey decodePrivateKey(final String pemEncoded) throws Exception {
//        byte[] encodedBytes = toEncodedBytes(pemEncoded);
//
//        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encodedBytes);
//        KeyFactory kf = KeyFactory.getInstance("RSA");
//        return kf.generatePrivate(keySpec);
//    }
//
//    private static byte[] toEncodedBytes(final String pemEncoded) {
//        final String normalizedPem = removeBeginEnd(pemEncoded);
//        return Base64.getDecoder().decode(normalizedPem);
//    }
//
//    private static String removeBeginEnd(String pem) {
//        pem = pem.replaceAll("-----BEGIN (.*)-----", "");
//        pem = pem.replaceAll("-----END (.*)----", "");
//        pem = pem.replaceAll("\r\n", "");
//        pem = pem.replaceAll("\n", "");
//        return pem.trim();
//    }
//    private static int currentTimeInSecs() {
//        long currentTimeMS = System.currentTimeMillis();
//        return (int) (currentTimeMS / 1000);
//    }
//
//}
