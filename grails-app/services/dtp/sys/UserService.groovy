package dtp.sys

import grails.transaction.Transactional

@Transactional
class UserService {

    def findAllUsersByRoleName(String roleName) {
    	
    	log.info('roleName: ' + roleName)

    	def role = dtp.sys.Role.findByAuthority(roleName)

    	log.info('role: ' + role)

  		def userroles = UserRole.findAllByRole(role)

  		def users = userroles.collect{[
  			username : it.user.username
  		]}

		log.info('users: ' + users)  		

  		return users
    }

    def getResolvedUsernames(String participant) {

    	log.info('participant: ' + participant)

    	def usernames = []

    	if (participant) {
    		if (participant.contains('ROLE')) {
    			usernames = findAllUsersByRoleName(participant)
    		}
    	}

    	return usernames
    }
}
