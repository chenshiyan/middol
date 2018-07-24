package dtp.sys


import grails.rest.*
import grails.transaction.Transactional

import static org.springframework.http.HttpStatus.NOT_FOUND
import static org.springframework.http.HttpStatus.NO_CONTENT
import static org.springframework.http.HttpStatus.OK

import grails.plugin.springsecurity.annotation.Secured

class UserController extends RestfulController {
    static responseFormats = ['json', 'xml']
    
    transient springSecurityService

    def userService

    UserController() {
        super(User)
    }

    @Override
    index(Integer max) {
        params.max = Math.min(max ?: 10, 100)

        println 'params : ' + params

        respond resource.list(params),
                [metadata: [total: countResources(), max: params.max, offset: params.offset ?: 0]]

    }

    @Transactional
    save(User user) {
        if (user == null) {
            dc.setRollbackOnly()
            render status: 404
            return
        }

        if (user.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond user.errors, view: 'create'
            return
        }

        user.save failOnError: true


        respond user, [status: OK, view: "show"]
    }

    @Transactional
    update(User user) {
        println params
        if (user == null) {
            transactionStatus.setRollbackOnly()
            render status: NOT_FOUND
            return
        }

        if (user.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond user.errors, view: 'edit'
            return
        }

        user.save failOnError: true

        respond user, [status: OK, view: "show"]
    }


    @Transactional
    delete(User user) {
        if (user == null) {
            transactionStatus.setRollbackOnly()
            render status: NOT_FOUND
            return
        }

        user.delete failOnError: true

        render status: NO_CONTENT
    }



    def changePassword(){
        def userInstance = User.findByUsername(request.JSON.username)
        if (!springSecurityService?.passwordEncoder.isPasswordValid(userInstance.password, request.JSON.oldPassword,null)) {
            render "errorpassword" 
            return -1
        }
        User.withTransaction{status->
        try {        
        userInstance.password = request.JSON.newPassword
        userInstance.save(flush:true,failOnError:true)
        } catch (Exception e) {
                e.printStackTrace()
                status.setRollbackOnly()
                render "error";
        }
    }
    render "success"  
    }

    def findAllUsersByRoleName() {
        render userService.findAllUsersByRoleName(params['roleName'])
    }
}
