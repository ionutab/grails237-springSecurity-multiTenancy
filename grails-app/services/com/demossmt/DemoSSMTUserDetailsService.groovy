package com.demossmt

import com.demossmt.auth.Tenant
import com.demossmt.auth.User
import grails.plugin.springsecurity.SpringSecurityUtils
import grails.plugin.springsecurity.userdetails.GormUserDetailsService
import grails.plugin.springsecurity.userdetails.GrailsUser
import grails.plugin.springsecurity.userdetails.GrailsUserDetailsService
import grails.transaction.Transactional
import org.codehaus.groovy.grails.web.util.WebUtils
import org.codehaus.groovy.grails.web.servlet.mvc.GrailsWebRequest
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.GrantedAuthorityImpl
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException

import java.security.Security

@Transactional
class DemoSSMTUserDetailsService extends GormUserDetailsService {

static final GrantedAuthority NO_ROLE = new SimpleGrantedAuthority(SpringSecurityUtils.NO_ROLE)

    def tenantResolverService = new TenantResolverService()

    UserDetails loadUserByUsername(String username, boolean loadRoles) throws UsernameNotFoundException {

        GrailsWebRequest webUtils = WebUtils.retrieveGrailsWebRequest()
        String requestServerName = webUtils.getRequest().getServerName()
        Tenant tenant = tenantResolverService.getTenantByWebDomainOnEnvironment(requestServerName)

        if(!tenant){
            log.warn "User not found: $username"
            throw new UsernameNotFoundException('User not found', username)
        }

        webUtils.getSession().setAttribute("TENANT_ID", tenant.id)

        User.withTransaction { status ->

            User user = User.findUserForLoginWithTenantId(tenant.id, username).get()

            if (!user) {
                log.warn "User not found: $username"
                throw new UsernameNotFoundException('User not found', username)
            }

            webUtils.getSession().setAttribute("USER_ID", user.id)

            Collection<GrantedAuthority> authorities = loadAuthorities(user, username, loadRoles)

            return new GrailsUser(user.username,
                    user.password,
                    user.enabled,
                    !user.accountExpired,
                    !user.passwordExpired,
                    !user.accountLocked,
                    authorities ?: [NO_ROLE],
                    user.id)

        }
    }
}
