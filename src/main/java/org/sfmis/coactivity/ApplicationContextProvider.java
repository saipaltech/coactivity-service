package org.sfmis.coactivity;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ApplicationContextProvider implements ApplicationContextAware {
	  private static ApplicationContext appContext;
	
	  /**
	    * Returns the Spring managed bean instance of the given class type if it exists.
	    * Returns null otherwise.
	    * @param beanClass
	    * @return
	    */
	  
	 public static <T extends Object> T getBean(Class<T> beanClass) {
	       return appContext.getBean(beanClass);
	   }
	
	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		
		ApplicationContextProvider.appContext = context;
		
	}
	

}
