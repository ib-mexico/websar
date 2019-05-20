package com.ibmexico.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.VariablesMap;
import org.thymeleaf.util.Validate;

@Service("homeService")
public class HomeService {

	@Autowired
	@Qualifier("sessionService")
	private SessionService sessionService;
	
	private VariablesMap<String,Object> variables;
	
	public final VariablesMap<String, Object> getVariables() {
        return this.variables;
    }
	
	public final void setVariable(final String name, final Object value) {
        Validate.notNull(name, "Variable name cannot be null");
        this.variables.put(name, value);
    }
}
