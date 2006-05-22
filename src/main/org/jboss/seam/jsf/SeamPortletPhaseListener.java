/*
 * JBoss, Home of Professional Open Source
 *
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jboss.seam.jsf;

import static javax.faces.event.PhaseId.INVOKE_APPLICATION;
import static javax.faces.event.PhaseId.RENDER_RESPONSE;
import static javax.faces.event.PhaseId.RESTORE_VIEW;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.seam.contexts.Lifecycle;
import org.jboss.seam.core.FacesMessages;
import org.jboss.seam.core.Manager;
import org.jboss.seam.util.Transactions;

/**
 * Manages the Seam contexts associated with a JSF portlet
 * request.
 * 
 * Manages the thread/context associations throughout the
 * lifecycle of the JSF request.
 *
 * @author Gavin King
 */
public class SeamPortletPhaseListener extends AbstractSeamPhaseListener
{

   private static final Log log = LogFactory.getLog( SeamPortletPhaseListener.class );

   public void beforePhase(PhaseEvent event)
   {
      log.trace( "before phase: " + event.getPhaseId() );
      
      Lifecycle.setPhaseId( event.getPhaseId() );

      if ( event.getPhaseId() == RESTORE_VIEW || event.getPhaseId() == RENDER_RESPONSE )
      {
         Lifecycle.beginRequest( event.getFacesContext().getExternalContext() );
      }
      
      if ( event.getPhaseId() == RENDER_RESPONSE )
      {
         restoreAnyConversationContext( event.getFacesContext() );         
         beforeRender(event);
      }

   }

   public void afterPhase(PhaseEvent event)
   {
      log.trace( "after phase: " + event.getPhaseId() );
      
      FacesContext facesContext = event.getFacesContext();
      
      if ( event.getPhaseId() == RESTORE_VIEW )
      {
         restoreAnyConversationContext(facesContext);
      }
      else if ( event.getPhaseId() == INVOKE_APPLICATION )
      {
         try
         {
            if ( Transactions.isTransactionMarkedRollback() )
            {
               FacesMessages.instance().addFromResourceBundle(
                        FacesMessage.SEVERITY_WARN, 
                        "org.jboss.seam.TransactionFailed", 
                        "Transaction failed"
                     );
            }
         }
         catch (Exception e) {} //swallow silently, not important
      }
      
      FacesMessages.afterPhase();
      
      if ( event.getPhaseId() == RENDER_RESPONSE )
      {
         Lifecycle.endRequest( facesContext.getExternalContext() );
      }
      else if ( event.getPhaseId() == INVOKE_APPLICATION || ( event.getPhaseId() != RESTORE_VIEW && facesContext.getResponseComplete() ) )
      {
         Manager.instance().beforeRedirect();
         storeAnyConversationContext(facesContext);
         Lifecycle.endRequest( facesContext.getExternalContext() );
      }

      Lifecycle.setPhaseId(null);
      
   }

}
