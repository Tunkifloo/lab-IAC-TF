pipeline {
    agent any
    
    tools {
        maven 'Maven'
        jdk 'JDK17'
    }
    
    environment {
        GIT_BRANCH = "${env.BRANCH_NAME ?: env.GIT_BRANCH ?: 'develop'}"
        DEPLOY_ENV = "${env.GIT_BRANCH == 'main' ? 'production' : 
                       env.GIT_BRANCH == 'develop' ? 'staging' : 'testing'}"
        
        APP_NAME = 'mtd-api'
        VERSION = getVersionFromBranch("${env.GIT_BRANCH}")
        DOCKER_IMAGE_TAG = "${APP_NAME}:${VERSION}-${BUILD_NUMBER}"
        
        // Credenciales
        CLOUDFLARE_CREDS = credentials('cloudflare-r2-credentials')
        MAIL_CREDS = credentials('spring-mail-credentials')
    }
    
    stages {
        stage('Checkout') {
            steps {
                checkout scm
                sh 'git branch --show-current'
                echo "Deploying to ${env.DEPLOY_ENV} environment from branch ${env.GIT_BRANCH}"
            }
        }
        
        stage('Build and Test') {
            steps {
                sh 'mvn clean package'
                
                // Verificar que el JAR fue creado correctamente
                sh 'ls -la target/*.jar'
            }
            post {
                success {
                    archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
                    junit '**/target/surefire-reports/TEST-*.xml'
                }
            }
        }
        
        stage('Build Docker Image') {
            steps {
                // Asegurarse de que el archivo JAR exista antes de construir la imagen
                sh 'ls -la target/*.jar || (echo "ERROR: JAR file not found"; exit 1)'
                
                sh "docker build -t ${DOCKER_IMAGE_TAG} ."
                sh "docker tag ${DOCKER_IMAGE_TAG} ${APP_NAME}:latest"
            }
        }
        
        stage('Deploy') {
            when {
                anyOf {
                    branch 'develop';
                    branch 'main'
                }
            }
            
            steps {
                script {
                    // Configura el entorno basado en la rama
                    def envFile = "${env.DEPLOY_ENV}.env"
                    
                    sh """
                        export CLOUDFLARE_R2_ACCESS_KEY=\${CLOUDFLARE_CREDS_USR}
                        export CLOUDFLARE_R2_SECRET_KEY=\${CLOUDFLARE_CREDS_PSW}
                        export CLOUDFLARE_R2_ENDPOINT=https://35a65665075e7977418ec554566af539.r2.cloudflarestorage.com
                        export CLOUDFLARE_R2_BUCKET_NAME=mtd-files-${env.DEPLOY_ENV}
                        export SPRING_MAIL_USERNAME=\${MAIL_CREDS_USR}
                        export SPRING_MAIL_PASSWORD=\${MAIL_CREDS_PSW}
                        
                        # Usa el archivo docker-compose específico del entorno si existe
                        if [ -f "docker-compose.${env.DEPLOY_ENV}.yml" ]; then
                            docker-compose -f docker-compose.${env.DEPLOY_ENV}.yml down
                            docker-compose -f docker-compose.${env.DEPLOY_ENV}.yml up -d
                        else
                            docker-compose down
                            docker-compose up -d
                        fi
                    """
                }
            }
        }
    }
    
    post {
        always {
            cleanWs()
        }
        success {
            echo "Pipeline completed successfully for ${env.GIT_BRANCH} branch!"
        }
        failure {
            echo "Pipeline failed for ${env.GIT_BRANCH} branch"
        }
    }
}

// Función para obtener la versión basada en la rama
def getVersionFromBranch(branch) {
    if (branch == 'main') {
        return "v1.0"
    } else if (branch == 'develop') {
        return "v1.0-SNAPSHOT"
    } else if (branch.startsWith('feature/')) {
        return "v1.0-${branch.split('/')[1]}"
    } else {
        return "v1.0-BETA"
    }
}