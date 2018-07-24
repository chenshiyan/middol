// Place your Spring DSL code here
import RestPagingCollectionRenderer

beans = {
	corsFilter(dtp.CorsFilter)

	for (domainClass in grailsApplication.domainClasses) {
	    "json${domainClass.shortName}CollectionRenderer"(RestPagingCollectionRenderer, domainClass.clazz)
	}	
}


