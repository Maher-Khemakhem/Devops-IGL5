
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

        stage('ARTIFACT CONSTRUCTION') {
            steps {
                echo 'ARTIFACT CONSTRUCTION...'
                sh 'mvn -B package -Dmaven.test.skip=true -Ptest-coverage'
            }
        }

        stage('UNIT TESTS') {
            steps {
                echo 'Launching Unit Tests...'
                sh 'mvn -B test'
            }
        }

        stage('MVN SONARQUBE') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    sh 'mvn -B sonar:sonar'
                }
            }
        }

        stage('PUBLISH TO NEXUS') {
            steps {
                echo 'Nexus publishing not configured yet'
                sh 'java -version'
            }
        }

        stage('BUILDING OUR IMAGE') {
            steps {
                script {
                    docker.build(
                        "${env.REGISTRY}:${env.BUILD_NUMBER}"
                    )
                }
            }
        }

        stage('DEPLOY OUR IMAGE') {
            steps {
                script {
                    docker.withRegistry(
                        'https://index.docker.io/v1/',
                        env.REGISTRY_CREDENTIAL
                    ) {
                        docker.image(
                            "${env.REGISTRY}:${env.BUILD_NUMBER}"
                        ).push()
                    }
                }
            }
        }
    }

    post {
        success {
            echo 'CI/CD PIPELINE SUCCESSFUL'
        }

        failure {
            echo 'CI/CD PIPELINE FAILED'
        }
    }
}
