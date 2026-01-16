package rugbyniela;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class TestPassword {
	public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        //String rawPassword = "12345678";//rugbyniela (admin)
        String rawPassword = "123";//rugbyniela (user)
        //String rawPassword = "password123";//practiarmar
        String encodedPassword = encoder.encode(rawPassword);
        
        System.out.println("------------------------------------------------");
        System.out.println("Tu Hash generado es: " + encodedPassword);
        System.out.println("------------------------------------------------");
    }
}
