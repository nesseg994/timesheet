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
	                branch "master"
	                branch "dev_1"
	                branch "dev_2"
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