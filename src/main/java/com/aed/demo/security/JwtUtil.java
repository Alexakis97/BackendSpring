package com.aed.demo.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtUtil {

    private String SECRET_KEY = "Gr^QwBL#z!ZK?4W+$PjjU&hnZ5GDDR2ZH#h$3g7YL9@d8RaD8MVT4M#yt=dkKv75#Tp5Z=k3rhmQ9TPSXDJUdGr2KH-cVb?q5Vf?KwsQgqrUhFPUcREwSP&N7DftYpQ&Ep^%@r5@LyeeeH#tG!57#EmZ7^KnP*=Ad?pdWX6tMnHX-X&8wmL6LY7ftFumsfsVjgPduzT@AeFe&#^qqaVemtSKsL-=s&HnzP7GXbQPwR=hk4hj=A9pkaDvBKmyxJqn^a9sfVj2cv@fmt=DCE2=HU7wJrH^ZYcrRMG5Z6gCVjX%rBK-?46UzPvBQtMbfF488*msuvPXPZswQTL9HMqftkRqpR=Zq7t5?8k_sXyVzWnH67=U4bpCbphRy^ts_xp**L2f!aRRE=zGp8_9LC?Fp!qy+dpN!*%sZu9&RSCHenNbCS?CcR7XYJzk3UH@78q#22fHpR9S6wGZt&kBtJsNFEyhATG9Y&E@9GJhXVPHrw2Y&wCmpeZwf?*$!UtjrRauw%v3qkrtz@GCYxZHaUE?5GKN+R5qGZXdJ+-!3-q!M8*gDZ6rp3SFtgwej?86mgD5Z55@4?epDCgC&E8$yVq53ZWKv^x*DPD&bs%cEy&cdtVyza=_r2m9EtXdHfsDqSkMqD^Rf=t_vVJzb98!!Mz^6tJCt^Mu?pyK-?@yG@!A2h-C-aSAu5^WG5fy@=E^9ZpMwHzbgqS+Y#@32FuEdg5Eu$myDqvzap25n*ebr9B!tpcruq8RjgA5$#yapAcHGPyX97rF$TtBc^SMfXy!n+dMEsh@LSa9h2Pfvf33qH8^2Danb6%F_s=P+Tu$Yz?#FwpkWgsfAV=3tgn?f#DnGE3QZrMz_YaCQTB-#Yg*HHfU9FPr5=qe8GTHGwqETE4NqCq4#9y^SbK-BMq^Lc+W^SR-$$VfAQXVmc67HKT3bp6QPwAX%Xa8nWu$W#jVH!SpBpwL+gNhG*B2pVupd5_VFff9sVg!rseBxUJ%DwFyFKGVEHJ&#e@@Eqba69kK!+hp&ZE-n&T?7%$XJjh=g8LzQNqD+G*^&E-GLMv%3%_rA%dpw9ZyuyBK3HBjcCf$^g^ZrR2bpXxYAR+2=yrGHdAj#&Fm*V?9MsT3VvV5c=fpU#daua%Y$w%D2tbrvFF_ur=eg6%aaK4axZjVKcN!V=BmX3_MkTHTAB5?u?dFr5!9MgrCUJ%$ggmN%a4Wg=6F?9gdXq5L+-wy6RMzUd%ApQZ+9B2w_B6ZYCwgvS*F_mU+eW$ywHzt?FMHsZr@mD#c!NrDzvD2!9rDGzDj@w9E_CAXGrnZS%-=5GxmC!+q?!B=+#EdKT5!2TM$XWmRJ&Nc*sryv88jn$SaEq4XQLmveRFgPS^E9Kr44qG-=*GdD9V^dYHgCw2&8YfzK-=L9fy$dkcDqzwL=A298dMSvT39ub$AxLtD#5NUC%869!&XeCceM7a7K!paegdAnUaQh^%WRCf=J#zu=6wX$4VP_yz=xjX$t9n^rpJR_APBSLaQMWqk5@EX6QFHQ*%ky$w=Gk5Dt4ckkVZnGV3k86GRPK8#@sH?CqnmdqFSKj$qrx=yfkpa6%k8L4Pwk+VDyYHAc9H&c_A5NBdkqzB^h%q!L?RFHJbse&MWnd4gHs!NKCY54X_*EBhrZX%n-R5#_&G5AqA2UE3pXDj#XuQXC5!f8?48yJZ?n&cr_c2fkhdALrTwy@pCP+8DeLDFTGk2n46*fw^xMdFcLS6@dUJVLyqx3-echp$#S9-Y3PD-A!pmcbMsk78?+Hf^yEEwS_6GyT_%e2LBapCukw+CHz#kKGYbTBdKWL3Mys2MKrsy7XKG5XBX#UzZ$ZG^k?$GM7qhQdAeHG36dEaH!-hce3JEC#r4f5b*6hBYgrFyrhBx$Y6ETGSReZgrbQT3$ThgQ@%^k+%YW^j_z6j_jDMAq7d9rzMvf=DyE6T*pV=$5m4a!#=sxpX$J-z-ZyeKnqZ?Q7ZUPhBbxT83P7=Knpz7-GW6vg7p-K8VDznj5=RqQL!_Lau+jz+%JVUwAEAm22acr%WXXceYHd5Z%e!Wa7NZ";

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    private Claims extractAllClaims(String token) {
    	return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    private String createToken(Map<String, Object> claims, String subject) {

        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
    
   
    
    
}