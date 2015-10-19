package com.demossmt

import grails.plugin.springsecurity.annotation.Secured

@Secured(['IS_AUTHENTICATED_ANONYMOUSLY'])
class RegisterController {

    def index() {
        render view: '/register/register'
    }
}
