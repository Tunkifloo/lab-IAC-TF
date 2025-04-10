pipeline {
    agent any
    
    tools {
    }
    
    parameters {
        booleanParam(name: 'DEPLOY_INFRA', defaultValue: false, description: 'Desplegar infraestructura con Terraform')
        choice(name: 'TF_ACTION', choices: ['plan', 'apply', 'destroy'], description: 'Acción de Terraform')
    }
    
    environment {
    JAVA_HOME = '/usr/lib/jvm/java-17-openjdk-amd64'
    PATH = "${JAVA_HOME}/bin:${env.PATH}"

    APP_NAME = 'mtd-api'
    
    CLOUDFLARE_CREDS = credentials('cloudflare-r2-credentials')
    MAIL_CREDS = credentials('spring-mail-credentials')
    
    TF_IN_AUTOMATION = 'true'
    }
    
    stages {
        stage('Set Environment') {
            steps {
                script {
                    env.GIT_BRANCH = env.BRANCH_NAME ?: env.GIT_BRANCH ?: 'develop'
                    env.DEPLOY_ENV = (env.GIT_BRANCH == 'main') ? 'production' :
                                    (env.GIT_BRANCH == 'develop') ? 'staging' : 'testing'
                    env.TF_WORKSPACE = env.DEPLOY_ENV
                    env.TF_VAR_FILE = "${env.DEPLOY_ENV}.tfvars"
                    env.VERSION = getVersionFromBranch(env.GIT_BRANCH)
                    env.DOCKER_IMAGE_TAG = "${env.APP_NAME}:${env.VERSION}-${env.BUILD_NUMBER}"
                }
            }
        }



        stage('Checkout') {
            steps {
                checkout scm
                sh 'git branch --show-current'
                echo "Deploying to ${env.DEPLOY_ENV} environment from branch ${env.GIT_BRANCH}"
            }
        }
        
        // ETAPAS DE TERRAFORM PARA INFRAESTRUCTURA
        stage('Terraform Init') {
            when {
                expression { params.DEPLOY_INFRA }
            }
            steps {
                echo "Inicializando Terraform para entorno ${env.DEPLOY_ENV}"
                sh 'terraform init'
            }
        }
        
        stage('Terraform Workspace') {
            when {
                expression { params.DEPLOY_INFRA }
            }
            steps {
                script {
                    try {
                        sh "terraform workspace select ${env.DEPLOY_ENV}"
                    } catch (Exception e) {
                        sh "terraform workspace new ${env.DEPLOY_ENV}"
                    }
                }
            }
        }
        
        stage('Terraform Plan') {
            when {
                expression { params.DEPLOY_INFRA && (params.TF_ACTION == 'plan' || params.TF_ACTION == 'apply') }
            }
            steps {
                echo "Planificando cambios de infraestructura para ${env.DEPLOY_ENV}"
                sh "terraform plan -var-file=${env.TF_VAR_FILE} -out=${env.DEPLOY_ENV}.tfplan"
            }
        }
        
        stage('Terraform Apply') {
            when {
                expression { params.DEPLOY_INFRA && params.TF_ACTION == 'apply' }
            }
            steps {
                echo "Aplicando cambios de infraestructura para ${env.DEPLOY_ENV}"
                sh "terraform apply ${env.DEPLOY_ENV}.tfplan"
                
                // Capturar las salidas para usar en etapas posteriores
                script {
                    env.APP_URL = sh(script: "terraform output -raw app_url || echo 'http://localhost:8080'", returnStdout: true).trim()
                    env.MYSQL_CONNECTION = sh(script: "terraform output -raw mysql_connection || echo 'mysql default'", returnStdout: true).trim()
                    
                    echo "URL de la aplicación: ${env.APP_URL}"
                    echo "Conexión MySQL: ${env.MYSQL_CONNECTION}" 
                }
            }
        }
        
        stage('Terraform Destroy') {
            when {
                expression { params.DEPLOY_INFRA && params.TF_ACTION == 'destroy' }
            }
            steps {
                input message: "⚠️ ¡CUIDADO! ¿Confirmar DESTRUCCIÓN de infraestructura en ${env.DEPLOY_ENV}?", ok: 'Destruir'
                echo "Destruyendo infraestructura en ${env.DEPLOY_ENV}"
                sh "terraform destroy -var-file=${env.TF_VAR_FILE} -auto-approve"
            }
        }
        
        // ETAPAS ORIGINALES PARA LA APLICACIÓN
        stage('Build and Test') {
            when {
                expression { !params.DEPLOY_INFRA || (params.DEPLOY_INFRA && params.TF_ACTION != 'destroy') }
                }
            steps {
                withEnv(["JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64", "PATH=/usr/lib/jvm/java-17-openjdk-amd64/bin:${env.PATH}"]) {
                sh 'java -version' // Verificación

                sh '''
                export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
                export PATH=$JAVA_HOME/bin:$PATH

                echo "JAVA_HOME is: $JAVA_HOME"
                which java
                java -version

                echo "Usando Maven instalado manualmente..."
                /usr/share/maven/bin/mvn clean package -DskipTests
                '''


                sh 'ls -la target/*.jar'
            }
        }
        post {
            success {
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }
}

        
        stage('Build Docker Image') {
            when {
                expression { !params.DEPLOY_INFRA || (params.DEPLOY_INFRA && params.TF_ACTION != 'destroy') }
            }
            steps {
                // Asegurarse de que el archivo JAR exista antes de construir la imagen
                sh 'ls -la target/*.jar || (echo "ERROR: JAR file not found"; exit 1)'
                
                sh "docker build -t ${DOCKER_IMAGE_TAG} ."
                sh "docker tag ${DOCKER_IMAGE_TAG} ${APP_NAME}:latest"
            }
        }
        
        stage('Deploy Application') {
            when {
                expression { 
                    (!params.DEPLOY_INFRA || (params.DEPLOY_INFRA && params.TF_ACTION != 'destroy')) &&
                    (env.GIT_BRANCH == 'develop' || env.GIT_BRANCH == 'main')
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
            // Limpiar archivos temporales pero no el workspace completo
            // para mantener los archivos de state de Terraform
            sh 'rm -f *.tfplan'
        }
        success {
            echo "Pipeline completed successfully for ${env.GIT_BRANCH} branch!"
            
            script {
                if (params.DEPLOY_INFRA && params.TF_ACTION == 'apply') {
                    echo "Infraestructura desplegada correctamente."
                    sh 'terraform output || echo "No hay outputs disponibles"'
                }
            }
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