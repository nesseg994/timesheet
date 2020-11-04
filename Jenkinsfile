pipeline {
    agent any
    triggers { pollSCM('H * * * *') }
    stages {
        stage('Clone and Clean Main Repo'){
            steps {
                deleteDir()
                bat "git clone https://github.com/nesseg994/timesheet.git"
                bat "mvn clean -f timesheet"
            }
        }
        
        stage('Test'){
            steps { 
                bat "mvn test -X -f timesheet"
            }
        }
        
        stage('Deploy') {
        	when {
        		anyOf{
	                branch "origin/master"
	            }
        	}
            steps {
                bat "mvn package -X -f timesheet"
                bat "mvn deploy -X -f timesheet"
                bat "mvn sonar:sonar -f timesheet"
            }
        }
    }
    
    post {
		success {
			script {
		    	if (env.BRANCH_NAME == 'dev_1' || ${env.BRANCH_NAME} == 'dev_1')
		    		deleteDir()
		    		bat "git init"
		        	bat "git fetch https://github.com/nesseg994/timesheet.git " + env.BRANCH_NAME + ":master"
		  	}
		}
		// failure block (send email)
	}
}