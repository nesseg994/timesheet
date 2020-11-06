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
        always {       
        	mail to: 'nesrine.eg94@gmail.com, nesrine.elghoul@esprit.tn',   
        	from: 'testjenkins994@gmail.com',  
            body: "${currentBuild.currentResult}: Job ${env.JOB_NAME} build ${env.BUILD_NUMBER}\n More info at: ${env.BUILD_URL}",
            subject: "Jenkins Build ${currentBuild.currentResult}: Job ${env.JOB_NAME}"
        }
    }
}