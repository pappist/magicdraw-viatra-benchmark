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
            	wrap([$class: 'Xvnc']) {
            		configFileProvider([configFile(fileId: 'org.jenkinsci.plugins.configfiles.maven.MavenSettingsConfig1377688925713', variable: 'MAVEN_SETTINGS')]) {
    					sh 'mvn clean install -Dmd.home=$MD_HOME -s $MAVEN_SETTINGS'
					}
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
    		archiveArtifacts 'com.incquerylabs.instaschema.test/results/**, benchmark/**'
    		junit '**/TEST-*.xml'
    	}
	}
}