variable "project_name" {
  description = "The name of the project"
  type        = string
}

variable "environment" {
  description = "The environment name"
  type        = string
}

variable "region" {
  description = "The GCP region"
  type        = string
}

variable "network_name" {
  description = "The name of the VPC network"
  type        = string
}

variable "service_account_email" {
  description = "The email of the VM Service Account"
  type        = string
}

variable "startup_script" {
  description = "The rendered startup script string"
  type        = string
}

variable "create_compute" {
  description = "Whether to create the compute instance (should be false in prod to stay in free tier)"
  type        = bool
  default     = true
}
