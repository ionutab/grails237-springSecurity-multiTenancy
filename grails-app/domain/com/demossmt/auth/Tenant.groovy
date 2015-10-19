package com.demossmt.auth

import groovy.transform.ToString
import org.jadira.usertype.dateandtime.joda.PersistentDateTime
import org.joda.time.DateTime

@ToString(includeNames = true, includeFields = true)
class Tenant {

      String subdomain
      String webdomain

      DateTime dateCreated
      DateTime lastUpdated

    static mapping = {
        dateCreated type: PersistentDateTime
        lastUpdated type: PersistentDateTime
    }

    static constraints = {
        subdomain unique: true
        subdomain matches: "[a-z]{1,10}"
    }
}
