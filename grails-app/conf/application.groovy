grails.databinding.dateFormats = [
        'yyyy-MM-dd', 'yyyyMMdd','MMddyyyy', 'yyyy-MM-dd HH:mm:ss.S', "yyyy-MM-dd'T'hh:mm:ss'Z'"
]

// Added by the Audit-Logging plugin:
//grails.plugin.auditLog.auditDomainClassName = 'dtp.AuditTrail'


grails.plugin.springsecurity.rest.token.storage.useGorm = true
grails.plugin.springsecurity.rest.token.storage.gorm.tokenDomainClassName = 'dtp.sys.AuthenticationToken'
// Added by the Spring Security Core plugin:
grails.plugin.springsecurity.securityConfigType = 'Requestmap'
grails.plugin.springsecurity.userLookup.userDomainClassName = 'dtp.sys.User'
grails.plugin.springsecurity.userLookup.authorityJoinClassName = 'dtp.sys.UserRole'
grails.plugin.springsecurity.authority.className = 'dtp.sys.Role'
grails.plugin.springsecurity.requestMap.className='dtp.sys.Requestmap'

// grails.plugin.springsecurity.controllerAnnotations.staticRules = [
// 	[pattern: '/',               access: ['permitAll']],
// 	[pattern: '/error',          access: ['permitAll']],
// 	[pattern: '/index',          access: ['permitAll']],
// 	[pattern: '/index.gsp',      access: ['permitAll']],
// 	[pattern: '/shutdown',       access: ['permitAll']],
// 	[pattern: '/assets/**',      access: ['permitAll']],
// 	[pattern: '/**/js/**',       access: ['permitAll']],
// 	[pattern: '/**/css/**',      access: ['permitAll']],
// 	[pattern: '/**/images/**',   access: ['permitAll']],
// 	[pattern: '/**/favicon.ico', access: ['permitAll']],
// 	//omitted the rest for brevity
// 	[pattern: '/api/logout', access: ['isAuthenticated()']]
// ]

grails.plugin.springsecurity.filterChain.chainMap = [
	[pattern: '/assets/**',      filters: 'none'],
	[pattern: '/**/js/**',       filters: 'none'],
	[pattern: '/**/css/**',      filters: 'none'],
	[pattern: '/**/images/**',   filters: 'none'],
	[pattern: '/**/favicon.ico', filters: 'none'],
    [pattern: '/api/**', filters: 'JOINED_FILTERS,-anonymousAuthenticationFilter,-exceptionTranslationFilter,-authenticationProcessingFilter,-securityContextPersistenceFilter,-rememberMeAuthenticationFilter'],	
	[pattern: '/**',             filters: 'JOINED_FILTERS']
]




