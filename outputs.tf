output "app_url" {
  description = "URL de la aplicación Spring Boot"
  value       = "http://localhost:${var.app_port[terraform.workspace]}"
}

output "jenkins_url" {
  description = "URL de Jenkins"
  value       = "http://localhost:${var.jenkins_port[terraform.workspace]}"
}

output "mysql_connection" {
  description = "Comando para conectarse a MySQL"
  value       = "mysql -h localhost -P ${var.mysql_port[terraform.workspace]} -u root -proot mtd"
}

output "environment" {
  description = "Entorno actual"
  value       = terraform.workspace
}