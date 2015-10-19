package com.demossmt

import com.demossmt.auth.Role
import com.demossmt.auth.User
import com.demossmt.auth.Tenant
import grails.plugin.springsecurity.annotation.Secured
import org.codehaus.groovy.grails.web.servlet.mvc.GrailsWebRequest
import org.codehaus.groovy.grails.web.util.WebUtils

@Secured(['IS_AUTHENTICATED_ANONYMOUSLY'])
class MainController {

    def tenantResolverService
    def springSecurityService

    def index(){
        GrailsWebRequest webUtils = WebUtils.retrieveGrailsWebRequest()
        String requestServerName = webUtils.getRequest().getServerName()

        def tenantSubdomain = tenantResolverService.isRequestServerNameWithTenant(requestServerName)
        log.debug("tenant prefix " + tenantSubdomain)

        if(tenantSubdomain != null){
            String correctTenantRedirectURL = tenantResolverService.getCorrectRedirectPath(tenantSubdomain)

            if(springSecurityService.isLoggedIn()){
            	redirect url: "http://" + correctTenantRedirectURL  + "/private"
            } else {
                log.info( "redirecting to: " + correctTenantRedirectURL  + "/login")
                redirect url: "http://" + correctTenantRedirectURL  + "/login"
                return
            }
        } else {
            render "<h3>hello from the public application landing page</h3>"
            return
        }
    }
}
