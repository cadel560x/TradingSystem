package ie.gmit.sw.fyp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class TradingSystemApplication {
	
	// Entry point
	public static void main(String[] args) {
		ApplicationContext applicationContext =  SpringApplication.run(TradingSystemApplication.class, args);
		
		for (String name: applicationContext.getBeanDefinitionNames()) {
            System.out.println(name);
        }
		
	}
	
} // end class TradingSystemApplication
