package com.demossmt


import com.demossmt.auth.Tenant
import grails.converters.JSON
import grails.plugin.springsecurity.SpringSecurityUtils
import org.codehaus.groovy.grails.web.servlet.mvc.GrailsWebRequest
import org.codehaus.groovy.grails.web.util.WebUtils
import org.springframework.security.access.annotation.Secured
import org.springframework.security.authentication.AccountExpiredException
import org.springframework.security.authentication.CredentialsExpiredException
import org.springframework.security.authentication.DisabledException
import org.springframework.security.authentication.LockedException
import org.springframework.security.core.context.SecurityContextHolder as SCH
import org.springframework.security.web.WebAttributes

@Secured('IS_AUTHENTICATED_ANONYMOUSLY')
class LoginController {

    /**
     * Dependency injection for the authenticationTrustResolver.
     */
    def authenticationTrustResolver

    /**
     * Dependency injection for the springSecurityService.
     */
    def springSecurityService
    def tenantResolverService

    /**
     * Default action; redirects to 'defaultTargetUrl' if logged in, /login/auth otherwise.
     */
    def index() {
        log.debug("LoginController.index")
        if (springSecurityService.isLoggedIn()) {
            log.debug("user is logged in")
            redirect uri: SpringSecurityUtils.securityConfig.successHandler.defaultTargetUrl
        } else {
            log.debug("user not logged in")
            redirect action: 'auth', params: params
        }
    }

    /**
     * Show the login page.
     */
    def auth() {
        def config = SpringSecurityUtils.securityConfig
        GrailsWebRequest webUtils = WebUtils.retrieveGrailsWebRequest()
        String requestServerName = webUtils.getRequest().getServerName()

        def tenantSubdomain = tenantResolverService.isRequestServerNameWithTenant(requestServerName)
        log.debug("tenant prefix " + tenantSubdomain)

        if(tenantSubdomain == null){
            redirect uri: "http://" + tenantResolverService.getCorrectRedirectPath(tenantSubdomain)
            return
        } else {
            if (springSecurityService.isLoggedIn()) {
                redirect uri: config.successHandler.defaultTargetUrl
                return
            } else {
                Tenant tenant = tenantResolverService.getTenantBySubdomain(tenantSubdomain)
                String view = 'auth'
                String postUrl = "${request.contextPath}${config.apf.filterProcessesUrl}"
                render view: view, model: [postUrl: postUrl, rememberMeParameter: config.rememberMe.parameter, tenant:tenant]
                return
            }
        }
    }

    /**
     * The redirect action for Ajax requests.
     */
    def authAjax() {
        if (request.xhr) {  // For any AJAX request or any condition you want
            session["SPRING_SECURITY_SAVED_REQUEST"] = null
        }
    }

    /**
     * Show denied page.
     */
    def denied() {
        if (springSecurityService.isLoggedIn() &&
                authenticationTrustResolver.isRememberMe(SCH.context?.authentication)) {
            // have cookie but the page is guarded with IS_AUTHENTICATED_FULLY
            redirect action: 'full', params: params
        }
    }

    /**
     * Login page for users with a remember-me cookie but accessing a IS_AUTHENTICATED_FULLY page.
     */
    def full() {
        redirect action: 'auth', params: params
    }

    /**
     * Callback after a failed login. Redirects to the auth page with a warning message.
     */
    def authfail() {
        String msg = ''
        def exception = session[WebAttributes.AUTHENTICATION_EXCEPTION]
        if (exception) {
            if (exception instanceof AccountExpiredException) {
                msg = g.message(code: "springSecurity.errors.login.expired")
            } else if (exception instanceof CredentialsExpiredException) {
                msg = g.message(code: "springSecurity.errors.login.passwordExpired")
            } else if (exception instanceof DisabledException) {
                msg = g.message(code: "springSecurity.errors.login.disabled")
            } else if (exception instanceof LockedException) {
                msg = g.message(code: "springSecurity.errors.login.locked")
            } else {
                msg = g.message(code: "springSecurity.errors.login.fail")
            }
        }

        if (springSecurityService.isAjax(request)) {
            render([error: msg] as JSON)
        } else {
            flash.warning = msg
            redirect action: 'auth', params: params
        }
    }

    /**
     * The Ajax success redirect url.
     */
    def ajaxSuccess() {
        render([success: true, username: springSecurityService.authentication.name] as JSON)
    }

    /**
     * The Ajax denied redirect url.
     */
    def ajaxDenied() {
        render([error: 'access denied'] as JSON)
    }
}
