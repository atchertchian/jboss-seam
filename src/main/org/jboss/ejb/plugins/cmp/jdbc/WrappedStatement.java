//$Id$
package org.jboss.ejb.plugins.cmp.jdbc;

import java.sql.Statement;

/**
 * A nice simple interface that allow us to get the original statement 
 * back from the wrapper.
 */
public interface WrappedStatement {
   public Statement getUnderlyingStatement();
}
