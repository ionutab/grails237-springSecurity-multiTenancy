import org.jadira.usertype.dateandtime.joda.PersistentDateTime
import org.jadira.usertype.dateandtime.joda.PersistentLocalDate
import org.jadira.usertype.dateandtime.joda.PersistentLocalTime
import org.joda.time.DateTime
import org.joda.time.LocalDate
import org.joda.time.LocalTime

grails.project.groupId = appName // change this to alter the default package name and Maven publishing destination

grails.mime.file.extensions = true // enables the parsing of file extensions from URLs into the request format
grails.mime.use.accept.header = false

grails.databinding.convertEmptyStringsToNull = true
grails.gorm.failOnError = false
grails.gorm.autoFlush = false

// The ACCEPT header will not be used for content negotiation for user agents containing the following strings (defaults to the 4 major rendering engines)
grails.mime.disable.accept.header.userAgents = ['Gecko', 'WebKit', 'Presto', 'Trident']
grails.mime.types = [ // the first one is the default format
    all:           '*/*', // 'all' maps to '*' or the first available format in withFormat
    atom:          'application/atom+xml',
    css:           'text/css',
    csv:           'text/csv',
    form:          'application/x-www-form-urlencoded',
    html:          ['text/html','application/xhtml+xml'],
    js:            'text/javascript',
    json:          ['application/json', 'text/json'],
    multipartForm: 'multipart/form-data',
    rss:           'application/rss+xml',
    text:          'text/plain',
    hal:           ['application/hal+json','application/hal+xml'],
    xml:           ['text/xml', 'application/xml']
]

// URL Mapping Cache Max Size, defaults to 5000
//grails.urlmapping.cache.maxsize = 1000
grails.resources.uri.prefix = 'static'
// What URL patterns should be processed by the resources plugin
grails.resources.adhoc.patterns = ['/images/*', '/css/*', '/js/*', '/plugins/*']
grails.resources.adhoc.includes = ['/images/**', '/css/**', '/js/**', '/plugins/**']

// Legacy setting for codec used to encode data with ${}
grails.views.default.codec = "html"

// The default scope for controllers. May be prototype, session or singleton.
// If unspecified, controllers are prototype scoped.
grails.controllers.defaultScope = 'singleton'

// GSP settings
grails {
    views {
        gsp {
            encoding = 'UTF-8'
            htmlcodec = 'xml' // use xml escaping instead of HTML4 escaping
            codecs {
                expression = 'html' // escapes values inside ${}
                scriptlet = 'html' // escapes output from scriptlets in GSPs
                taglib = 'none' // escapes output from taglibs
                staticparts = 'none' // escapes output from static template parts
            }
        }
        // escapes all not-encoded output at final stage of outputting
        // filteringCodecForContentType.'text/html' = 'html'
    }
}


grails.converters.encoding = "UTF-8"
// scaffolding templates configuration
grails.scaffolding.templates.domainSuffix = 'Instance'

// Set to false to use the new Grails 1.2 JSONBuilder in the render method
grails.json.legacy.builder = false
// enabled native2ascii conversion of i18n properties files
grails.enable.native2ascii = true
// packages to include in Spring bean scanning
grails.spring.bean.packages = []
// whether to disable processing of multi part requests
grails.web.disable.multipart=false

// request parameters to mask when logging exceptions
grails.exceptionresolver.params.exclude = ['password']

// configure auto-caching of queries by default (if false you can cache individual queries with 'cache: true')
grails.hibernate.cache.queries = false

// configure passing transaction's read-only attribute to Hibernate session, queries and criterias
// set "singleSession = false" OSIV mode in hibernate configuration after enabling
grails.hibernate.pass.readonly = false
// configure passing read-only to OSIV session by default, requires "singleSession = false" OSIV mode
grails.hibernate.osiv.readonly = false

grails.gorm.default.mapping = {
    "user-type" type: PersistentDateTime, class: DateTime
    "user-type" type: PersistentLocalDate, class: LocalDate
    "user-type" type: PersistentLocalTime, class: LocalTime
}

environments {
    development {
        grails.logging.jul.usebridge = true
    }
    production {
        grails.logging.jul.usebridge = false
        // TODO: grails.serverURL = "http://www.changeme.com"
    }
}


log4j = {

    appenders {
        def logPattern = "[%d{dd/MM/yyyy HH:mm:ss,SSS}] %5p %c{2} - %m%n"
        console name: 'stdout', layout: pattern(conversionPattern: logPattern)
    }
    error 'org.codehaus.groovy.grails.web.servlet',
            'org.codehaus.groovy.grails.orm.hibernate',
            'org.springframework',
            'net.sf.ehcache.hibernate',
            'org.hibernate',
            'org.codehaus.groovy.grails.web.mapping.filter',
            'org.codehaus.groovy.grails.web.mapping',
            'org.codehaus.groovy.grails.commons',
            'org.codehaus.groovy.grails.plugins',
            'org.codehaus.groovy.grails.web.pages',
            'org.codehaus.groovy.grails.web.sitemesh',
            'grails.plugin.springsecurity',
            'org.hibernate.type.descriptor.sql.*',
            'org.hibernate.type.descriptor.sql.BasicBinder'

    debug 'grails.plugin.springcache',
            'com.demossmt',
            'org.apache.http.headers',
            'grails.app.services',
            'grails.app.domain',
            'grails.app.controllers',
    root {
        error 'stdout'
    }
}

// Added by the Spring Security Core plugin:
// All possible security configurations here
// https://github.com/grails-plugins/grails-spring-security-core/blob/master/grails-app/conf/DefaultSecurityConfig.groovy
grails.plugin.springsecurity.active = true
grails.plugin.springsecurity.auth.loginFormUrl = '/login'
grails.plugin.springsecurity.password.algorithm = 'SHA-256'
grails.plugin.springsecurity.password.hash.iterations = 1
grails.plugin.springsecurity.userLookup.userDomainClassName = 'com.demossmt.auth.User'
grails.plugin.springsecurity.userLookup.authorityJoinClassName = 'com.demossmt.auth.UserRole'
grails.plugin.springsecurity.authority.className = 'com.demossmt.auth.Role'
grails.plugin.springsecurity.securityConfigType = "Annotation"
grails.plugin.springsecurity.controllerAnnotations.staticRules = [
	'/':                              ['IS_AUTHENTICATED_ANONYMOUSLY'],
	'/index':                         ['IS_AUTHENTICATED_ANONYMOUSLY'],
	'/index.gsp':                     ['IS_AUTHENTICATED_ANONYMOUSLY'],
	'/**/js/**':                      ['IS_AUTHENTICATED_ANONYMOUSLY'],
	'/**/css/**':                     ['IS_AUTHENTICATED_ANONYMOUSLY'],
	'/**/images/**':                  ['IS_AUTHENTICATED_ANONYMOUSLY'],
	'/**/favicon.ico':                ['IS_AUTHENTICATED_ANONYMOUSLY'],
    "/main/info/**":                  ['IS_AUTHENTICATED_ANONYMOUSLY'],
    '/dbconsole/**':                  ['IS_AUTHENTICATED_ANONYMOUSLY'],
    '/dbdoc/**':                      ['IS_AUTHENTICATED_ANONYMOUSLY']
]

