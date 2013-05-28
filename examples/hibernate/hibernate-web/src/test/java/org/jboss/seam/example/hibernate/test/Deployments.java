package org.jboss.seam.example.hibernate.test;

import java.io.File;
import org.jboss.seam.example.hibernate.Booking;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;

public class Deployments {

    public static WebArchive hibernateDeployment() {

        // use profiles defined in 'maven.profiles' property in pom.xml
        String profilesString = System.getProperty("maven.profiles");
        String[] profiles = profilesString != null? profilesString.split(", ?") : new String[0];
        File[] libs = Maven.resolver().loadPomFromFile("pom.xml", profiles).importRuntimeDependencies().asFile();

        // Get File reference to src/main/webapp/WEB-INF, to avoid duplication of resources for the test archive
        // Depends on shrinkwrap-resolver-maven-plugin to set the property
        File webInf = new File(new File("pom.xml").getParentFile(), "src/main/webapp/WEB-INF");

        return ShrinkWrap.create(WebArchive.class, "hibernate-web.war")
                .addPackage(Booking.class.getPackage())
                
                // copy classpath resources to WAR classpath
                .addAsWebInfResource("import.sql", "classes/import.sql")
                .addAsWebInfResource("seam.properties", "classes/seam.properties")
                .addAsWebInfResource("hibernate.cfg.xml", "classes/hibernate.cfg.xml")
                
                // copy files from src/main/webapp to test-archive's WEB-INF
                .addAsWebInfResource(new File(webInf, "components.xml"))
                .addAsWebInfResource(new File(webInf, "jboss-deployment-structure.xml"))
                .addAsWebInfResource(new File(webInf, "pages.xml"))
                
                // modified web.xml, defines only MockSeamListener
                .addAsWebInfResource("web.xml")
                .addAsLibraries(libs);
    }
}
