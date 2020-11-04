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
            
            post {
				success {
					script {
				    	if (env.BRANCH_NAME == 'dev_1' || env.BRANCH_NAME == 'dev_2') {
				        	bat "git merge -X " + env.BRANCH_NAME + " master"
				        }
				  	}
				}
				// failure block (send email)
			}
        }
        
        stage('Deploy') {
        	when {
        		anyOf{
	                branch "master"
	            }
        	}
            steps {
                bat "mvn package -X -f timesheet"
                bat "mvn deploy -X -f timesheet"
                bat "mvn sonar:sonar -f timesheet"
            }
        }
    }
}