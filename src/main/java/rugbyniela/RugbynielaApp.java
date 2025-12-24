package rugbyniela;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.cmeza.sdgenerator.annotation.SDGenerator;

@SDGenerator(
        entityPackage = "rugbyniela.entity.pojo",
        repositoryPackage = "rugbyniela.repository",
        onlyAnnotations = false,
        debug = false,
        overwrite = false,
        lombokAnnotations = false
)
@SpringBootApplication
@EnableScheduling //To allow process in background
public class RugbynielaApp {

	public static void main(String[] args) {
		SpringApplication.run(RugbynielaApp.class, args);
	}

}
