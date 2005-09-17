/*
 * JBoss, Home of Professional Open Source
 *
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */ 
package com.jboss.dvd.ejb;

import java.util.Map;
import java.util.List;

public interface Search
{
    public boolean getHasResults();
    public Map<String,Integer> getCategories();
    
    public String nextPage();
    public String prevPage(); 
    public boolean isLastPage();
    public boolean isFirstPage();

    public String doSearch();
    public String addToCart();
    public String checkout();

    public void destroy();
}
