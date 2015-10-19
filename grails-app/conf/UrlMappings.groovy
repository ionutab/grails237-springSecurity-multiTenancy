class UrlMappings {

	static mappings = {
        "/" (controller: "main", action: "index")
        "/dashboard"	(controller: "main", action: "dashboard")

        name 'about':"/about"               (view:"/main/info/about")

        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }

        name 'login':'/login' (controller: 'login', action: 'auth')
        name 'register':"/register" (controller: 'register', action: 'index')

        "500"(view:'/error')
	}
}
