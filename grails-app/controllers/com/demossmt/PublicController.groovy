package com.demossmt

import grails.plugin.springsecurity.annotation.Secured

@Secured(['IS_AUTHENTICATED_ANONYMOUSLY'])
class PublicController {

    def landing(){
        render "<h1>LANDING</h1>"
    }

    def about() {
        render "hello from the about page"
    }

    def about2() {
        render "hello from the about page 2"
    }

}
