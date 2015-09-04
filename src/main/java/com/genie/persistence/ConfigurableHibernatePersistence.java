/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.genie.persistence;

import org.hibernate.Interceptor;
import org.hibernate.ejb.HibernatePersistence;

/**
 *
 * @author ccubukcu
 */
public class ConfigurableHibernatePersistence extends HibernatePersistence {

    private Interceptor interceptor;

    public Interceptor getInterceptor() {
        return interceptor;
    }

    public void setInterceptor(Interceptor interceptor) {
        this.interceptor = interceptor;
    }
}
