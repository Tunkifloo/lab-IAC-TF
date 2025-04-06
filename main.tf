terraform {
  required_providers {
    docker = {
      source  = "kreuzwerker/docker"
      version = "3.0.2"
    }
  }
}

provider "docker" {}

# Red para todos los contenedores
resource "docker_network" "mtd_network" {
  name = "mtd_network_${terraform.workspace}"
}

# Volúmenes persistentes
resource "docker_volume" "jenkins_home" {
  name = "jenkins_home_${terraform.workspace}"
}

resource "docker_volume" "db_data" {
  name = "db_data_${terraform.workspace}"
}

# Imagen de MySQL
resource "docker_image" "mysql" {
  name = "mysql:8.0.36"
}

# Base de datos MySQL
resource "docker_container" "mtd_db" {
  name  = "mtd_db_${terraform.workspace}"
  image = docker_image.mysql.image_id
  
  ports {
    internal = 3306
    external = var.mysql_port[terraform.workspace]
  }
  
  env = [
    "MYSQL_ROOT_PASSWORD=root",
    "MYSQL_PASSWORD=root",
    "MYSQL_DATABASE=mtd"
  ]
  
  healthcheck {
    test     = ["CMD", "mysqladmin", "ping", "-h", "localhost"]
    interval = "10s"
    timeout  = "5s"
    retries  = 10
  }
  
  volumes {
    volume_name    = docker_volume.db_data.name
    container_path = "/var/lib/mysql"
  }
  
  networks_advanced {
    name = docker_network.mtd_network.name
  }
}

# Imagen de Jenkins
resource "docker_image" "jenkins" {
  name = "jenkins/jenkins:lts"
}

# Contenedor Jenkins
resource "docker_container" "jenkins" {
  name  = "jenkins_${terraform.workspace}"
  image = docker_image.jenkins.image_id
  
  ports {
    internal = 8080
    external = var.jenkins_port[terraform.workspace]
  }
  
  ports {
    internal = 50000
    external = var.jenkins_agent_port[terraform.workspace]
  }
  
  env = [
    "JAVA_OPTS=-Djenkins.install.runSetupWizard=false"
  ]
  
  volumes {
    volume_name    = docker_volume.jenkins_home.name
    container_path = "/var/jenkins_home"
  }
  
  volumes {
    host_path      = "/var/run/docker.sock"
    container_path = "/var/run/docker.sock"
  }
  
  networks_advanced {
    name = docker_network.mtd_network.name
  }
}

# Construir la imagen de la aplicación
resource "docker_image" "mtd_app" {
  name = "mtd-api:${terraform.workspace}"
  build {
    context = "."
    dockerfile = "Dockerfile"
  }
}

# Contenedor de la aplicación
resource "docker_container" "mtd_app" {
  name  = "mtd_app_${terraform.workspace}"
  image = docker_image.mtd_app.image_id
  
  ports {
    internal = 8080
    external = var.app_port[terraform.workspace]
  }
  
  env = [
    "SPRING_DATASOURCE_URL=jdbc:mysql://mtd_db_${terraform.workspace}:3306/mtd?createDatabaseIfNotExist=true&serverTimezone=UTC",
    "SPRING_DATASOURCE_USERNAME=root",
    "SPRING_DATASOURCE_PASSWORD=root",
    "SPRING_JPA_HIBERNATE_DDL_AUTO=update",
    "SPRING_DATASOURCE_INITIALIZATION-MODE=always",
    "CLOUDFLARE_R2_ACCESS_KEY=${var.cloudflare_r2_access_key}",
    "CLOUDFLARE_R2_SECRET_KEY=${var.cloudflare_r2_secret_key}",
    "CLOUDFLARE_R2_BUCKET_NAME=mtd-files-${terraform.workspace}",
    "CLOUDFLARE_R2_ENDPOINT=${var.cloudflare_r2_endpoint}",
    "SPRING_MAIL_USERNAME=${var.spring_mail_username}",
    "SPRING_MAIL_PASSWORD=${var.spring_mail_password}",
    "SPRING_PROFILES_ACTIVE=${terraform.workspace}"
  ]
  
  depends_on = [docker_container.mtd_db]
  
  networks_advanced {
    name = docker_network.mtd_network.name
  }
}