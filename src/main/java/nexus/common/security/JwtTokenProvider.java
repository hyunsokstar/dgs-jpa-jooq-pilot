        package nexus.common.security;

        import io.jsonwebtoken.Jwts;
        import io.jsonwebtoken.SignatureAlgorithm;
        import io.jsonwebtoken.security.Keys;
        import jakarta.annotation.PostConstruct;
        import org.springframework.stereotype.Component;

        import javax.crypto.SecretKey;
        import java.util.Date;

        @Component
        public class JwtTokenProvider {

            private SecretKey secretKey; // ✅ 타입 변경
            private final long validityInMilliseconds = 3600000; // 1시간

            @PostConstruct
            public void init() {
                // ✅ 안전한 키 자동 생성 (HS256에 맞는 길이)
                this.secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
            }

            public String createToken(String email) {
                Date now = new Date();
                Date expiry = new Date(now.getTime() + validityInMilliseconds);

                return Jwts.builder()
                        .setSubject(email)
                        .setIssuedAt(now)
                        .setExpiration(expiry)
                        .signWith(secretKey) // ✅ 변경된 키로 서명
                        .compact();
            }
        }
