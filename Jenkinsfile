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
		    		bat "git checkout master"
		        	bat "git merge " + env.BRANCH_NAME
		  	}
		}
		// failure block (send email)
	}
}