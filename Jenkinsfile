
pipeline {
    agent any

    tools {
        maven 'Maven3'
    }

    options {
        skipDefaultCheckout()
        timestamps()
    }

    environment {
        REGISTRY = 'maherkh/spring-boot-app'
        REGISTRY_CREDENTIAL = 'dockerhub'
    }

    stages {
        stage('CHECKOUT GIT') {
            steps {
                checkout scm
            }
        }

        stage('CHECK MAVEN') {
            steps {
                sh 'java -version'
                sh 'mvn -version'
            }
        }

        stage('MVN CLEAN') {
            steps {
                sh 'mvn -B clean'
            }
        }

        // Keep all your other stages here
    }
}
