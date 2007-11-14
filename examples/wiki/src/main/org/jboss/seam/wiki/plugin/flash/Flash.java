package org.jboss.seam.wiki.plugin.flash;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.ScopeType;
import org.jboss.seam.log.Log;

import java.io.Serializable;
import java.util.Arrays;
import java.net.URI;

@Name("flash")
@Scope(ScopeType.PAGE)
public class Flash implements Serializable {

    @In
    FlashPreferences flashPreferences;

    @Logger
    Log log;

    public boolean isURLAllowed() {
        String allowedDomains = flashPreferences.getAllowedDomains();
        if (allowedDomains == null || allowedDomains.length() == 0) return false;

        String desiredDomainName;
        try {
            URI uri = new URI(flashPreferences.getFlashURL());
            desiredDomainName = uri.getHost();
        } catch (Exception ex) {
            log.debug("Exception parsing flash movie URL into URI: " + ex.getMessage());
            return false;
        }
        allowedDomains = allowedDomains.replaceAll("\\s", ""); // Remove spaces
        String[] allowedDomainNames = allowedDomains.split(",");
        if (desiredDomainName == null || desiredDomainName.length() == 0) return true;

        Arrays.sort(allowedDomainNames);
        return Arrays.binarySearch(allowedDomainNames, desiredDomainName) >= 0;
    }
}
