variable "app_port" {
  description = "Puerto externo para la aplicación Spring Boot"
  type        = map(number)
}

variable "mysql_port" {
  description = "Puerto externo para MySQL"
  type        = map(number)
}

variable "jenkins_port" {
  description = "Puerto externo para Jenkins"
  type        = map(number)
}

variable "jenkins_agent_port" {
  description = "Puerto externo para el agente Jenkins"
  type        = map(number)
}

variable "cloudflare_r2_access_key" {
  description = "Cloudflare R2 Access Key"
  type        = string
  sensitive   = true
}

variable "cloudflare_r2_secret_key" {
  description = "Cloudflare R2 Secret Key"
  type        = string
  sensitive   = true
}

variable "cloudflare_r2_endpoint" {
  description = "Cloudflare R2 Endpoint"
  type        = string
}

variable "spring_mail_username" {
  description = "Username para el servicio de correo"
  type        = string
  sensitive   = true
}

variable "spring_mail_password" {
  description = "Password para el servicio de correo"
  type        = string
  sensitive   = true
}