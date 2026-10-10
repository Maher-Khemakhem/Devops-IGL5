
pipeline {
    agent any

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

        stage('MVN CLEAN') {
            steps {
                sh 'mvn -B clean'
            }
        }

        stage('UNIT TESTS') {
            steps {
                sh 'mvn -B test'
            }
        }

        stage('ARTIFACT CONSTRUCTION') {
            steps {
                sh 'mvn -B package -DskipTests'
            }
        }

        stage('SONARQUBE ANALYSIS') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    sh 'mvn -B sonar:sonar'
                }
            }
        }

        stage('SONARQUBE QUALITY GATE') {
            steps {
                timeout(time: 10, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        stage('PUBLISH TO NEXUS') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: 'nexus-credentials',
                        usernameVariable: 'NEXUS_USER',
                        passwordVariable: 'NEXUS_PASS'
                    )
                ]) {
                    configFileProvider([
                        configFile(
                            fileId: 'nexus-settings',
                            variable: 'MAVEN_SETTINGS'
                        )
                    ]) {
                        sh 'mvn -B -s "$MAVEN_SETTINGS" deploy -DskipTests'
                    }
                }
            }
        }

        stage('BUILD DOCKER IMAGE') {
            steps {
                script {
                    docker.build("${REGISTRY}:${BUILD_NUMBER}")
                }
            }
        }

        stage('PUSH DOCKER IMAGE') {
            steps {
                script {
                    docker.withRegistry(
                        'https://index.docker.io/v1/',
                        REGISTRY_CREDENTIAL
                    ) {
                        docker.image(
                            "${REGISTRY}:${BUILD_NUMBER}"
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
