package com.demossmt

import grails.plugin.springsecurity.annotation.Secured

class PrivateController {

    @Secured(['ROLE_ADMIN'])
    def index() {
        render view: '/index'
    }

}
