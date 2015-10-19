package com.demossmt.auth

class User {

	transient springSecurityService

	String username
	String password
	boolean enabled = true
	boolean accountExpired
	boolean accountLocked
	boolean passwordExpired

	String firstName
	String lastName
	String emailAddress

	static transients = ['springSecurityService']

	static belongsTo = [tenant:Tenant]

	static constraints = {
		username blank: false, unique: true
		password blank: false
	}

	static mapping = {
		password column: '`password`'
	}

	Set<Role> getAuthorities() {
		UserRole.findAllByUser(this).collect { it.role } as Set
	}

	def beforeInsert() {
		encodePassword()
	}

	def beforeUpdate() {
		if (isDirty('password')) {
			encodePassword()
		}
	}

	protected void encodePassword() {
		password = springSecurityService.encodePassword(password)
	}

    def getFullName(){
        return firstName + " " + lastName
    }
	
    static namedQueries = {

        tenanted { tenantId ->
            tenant {
                eq('id', tenantId)
            }
        }

        findUserForLogin{ String username ->
            or {
                eq('username', username)
                eq('emailAddress', username)
            }
        }

        findUserForLoginWithTenantId{ tenantId, username ->

            tenanted(tenantId)
            or {
                eq('username', username)
                eq('emailAddress', username)
            }
        }
    }
}
