import com.demossmt.auth.*

class BootStrap {

    def init = { servletContext ->

        def tenantA = Tenant.findOrSaveWhere(subdomain: 'a', webdomain:'a.demossmt.com')
        def tenantB = Tenant.findOrSaveWhere(subdomain: 'b', webdomain:'b.demossmt.com')
        def tenantC = Tenant.findOrSaveWhere(subdomain: 'c', webdomain:'c.demossmt.com')
        def adminRole = Role.findOrSaveWhere(authority: 'ROLE_ADMIN')
        def userRole = Role.findOrSaveWhere(authority: 'ROLE_USER')
        def userA = User.findOrSaveWhere(username: 'usera', password: 'password', firstName: 'UserA-firstname', lastName: 'UserA-firstname', emailAddress: 'usera@demossmt.com', tenant: tenantA)
        def userB = User.findOrSaveWhere(username: 'userb', password: 'password', firstName: 'UserB-firstname', lastName: 'UserB-firstname', emailAddress: 'userb@demossmt.com', tenant: tenantB)
        def userC = User.findOrSaveWhere(username: 'userc', password: 'password', firstName: 'UserC-firstname', lastName: 'UserC-firstname', emailAddress: 'userc@demossmt.com', tenant: tenantB)
        if(!userA.authorities.contains(adminRole)){
            UserRole.create(userA, adminRole, true)
        }
        if(!userB.authorities.contains(adminRole)){
            UserRole.create(userB, adminRole, true)
        }
        if(!userC.authorities.contains(adminRole)){
            UserRole.create(userC, adminRole, true)
        }
    }
    def destroy = {
    }
}

