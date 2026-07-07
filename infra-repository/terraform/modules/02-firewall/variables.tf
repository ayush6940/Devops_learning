variable "project_name" {
  description = "The name of the project"
  type        = string
}

variable "environment" {
  description = "The environment name"
  type        = string
}

variable "network_name" {
  description = "The name of the VPC network to apply the firewall to"
  type        = string
}
