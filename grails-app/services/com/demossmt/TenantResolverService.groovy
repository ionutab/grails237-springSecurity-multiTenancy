package com.demossmt

import com.demossmt.auth.Tenant
import grails.util.Environment
import org.codehaus.groovy.grails.commons.ApplicationHolder

/**
 * TenantResolverService
 * A service class encapsulates the core business logic of a Grails application
 */
class TenantResolverService {

    def grailsApplication = grails.util.Holders.grailsApplication

    def tenantService

    def getDomainURLForCurrentEnvironment(){
        String appDomainURLDomainURL =  grailsApplication.getMetadata().get('app.domain.short')
        if (Environment.current == Environment.DEVELOPMENT || Environment.current == Environment.TEST) {
            appDomainURLDomainURL = grailsApplication.getMetadata().get('app.development.domain.short')
        }
        return appDomainURLDomainURL
    }

    def getDomainAndPortURLForCurrentEnvironment(){
        String appDomainURLDomainURL =  grailsApplication.getMetadata().get('app.domain.short')
        if (Environment.current == Environment.DEVELOPMENT || Environment.current == Environment.TEST) {
            appDomainURLDomainURL = grailsApplication.getMetadata().get('app.development.domainAndPort.short')
        }
        return appDomainURLDomainURL
    }

    def getCorrectRedirectPath(tenantSubdomain){
        Tenant tenant = getTenantBySubdomain(tenantSubdomain)
        if(tenant != null){
            String tenantLoginURL = tenantSubdomain + "." + getDomainAndPortURLForCurrentEnvironment()
            return tenantLoginURL
        } else {
            return getDomainAndPortURLForCurrentEnvironment()
        }
    }

    def getTenantByWebDomainOnEnvironment(String requestServerName){
        log.info("REQUEST SERVER NAME: ": requestServerName)
        if (Environment.current == Environment.DEVELOPMENT || Environment.current == Environment.TEST) {
            requestServerName = requestServerName.replace(grailsApplication.getMetadata().get('app.localhost.synonym'), grailsApplication.getMetadata().get('app.name'))
        }
        return Tenant.findByWebdomainIlike("%" + requestServerName)
    }

    def getTenantBySubdomain(String subdomain){
        return Tenant.findBySubdomain(subdomain)
    }

    def isThisProductionEnvironment(){
        if (Environment.current == Environment.DEVELOPMENT || Environment.current == Environment.TEST) {
            return false
        }
        return true
    }

    String isRequestServerNameWithTenant(String requestServerName){
        String domainForCurrentEnvironment = getDomainURLForCurrentEnvironment()
        if(requestServerName.startsWith("www.")){
            requestServerName = requestServerName.substring(4)
        }

        if(requestServerName == domainForCurrentEnvironment){
            return null
        }
        int positionOfDomain = requestServerName.indexOf(domainForCurrentEnvironment)
        if(positionOfDomain >= 0){
            requestServerName = requestServerName.substring(0, positionOfDomain - 1)
            return requestServerName
        }
        return null
    }
}
