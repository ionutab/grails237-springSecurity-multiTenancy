import com.demossmt.DemoSSMTUserDetailsService

// Place your Spring DSL code here
beans = {
    userDetailsService(com.demossmt.DemoSSMTUserDetailsService){
        grailsApplication = ref('grailsApplication')
    }
}
