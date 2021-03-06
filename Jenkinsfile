// Tell Jenkins how to build projects from this repository
pipeline {
	agent {
		label 'magicdraw'
	} 

    // Keep only the last 15 builds
	options {
		buildDiscarder(logRotator(numToKeepStr: '15'))
	}
	
	tools { 
        maven 'Maven 3.3.9' 
        jdk 'Oracle JDK 8' 
    }
	 
    stages { 
        stage('Build') { 
            steps {
        		configFileProvider([configFile(fileId: 'org.jenkinsci.plugins.configfiles.maven.MavenSettingsConfig1377688925713', variable: 'MAVEN_SETTINGS')]) {
					sh 'mvn clean install -Dmd.home=$MD_HOME -s $MAVEN_SETTINGS'
				}
            }
		}
		stage('Benchmark') {
            steps {
            	wrap([$class: 'Xvnc']) {
					sh './com.incquerylabs.magicdraw.benchmark/run.sh'
            	}
			}
		}
		stage('Report') {
			steps {
			    sh './benchmark/dep-mondo-sam.sh'
				sh './benchmark/convert_results.sh'
				sh './benchmark/report.sh'
			}
		}
    }
    
    post {
    	always {
    		archiveArtifacts 'com.incquerylabs.magicdraw.benchmark/results/**, benchmark/**'
    	}
        success {
            slackSend channel: "magicdraw-notificatio", 
    			color: "good",
				message: "Build Successful - ${env.JOB_NAME} ${env.BUILD_NUMBER} (<${env.BUILD_URL}|Open>)",
    			teamDomain: "incquerylabs",
    			tokenCredentialId: "6ff98023-8c20-4d9c-821a-b769b0ea0fad" 
        }
		unstable {
	   		slackSend channel: "magicdraw-notificatio", 
    			color: "warning",
    			message: "Build Unstable - ${env.JOB_NAME} ${env.BUILD_NUMBER} (<${env.BUILD_URL}|Open>)",
    			teamDomain: "incquerylabs",
    			tokenCredentialId: "6ff98023-8c20-4d9c-821a-b769b0ea0fad"
    	}
		failure {
    		slackSend channel: "magicdraw-notificatio", 
    			color: "danger",
    			message: "Build Failed - ${env.JOB_NAME} ${env.BUILD_NUMBER} (<${env.BUILD_URL}|Open>)",
    			teamDomain: "incquerylabs",
    			tokenCredentialId: "6ff98023-8c20-4d9c-821a-b769b0ea0fad"
		}
	}
}