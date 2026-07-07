output "vm_external_ip" {
  description = "The external public IP of the VM"
  value       = length(google_compute_instance.vm_instance) > 0 ? google_compute_instance.vm_instance[0].network_interface[0].access_config[0].nat_ip : null
}

output "vm_internal_ip" {
  description = "The internal IP of the VM within the VPC"
  value       = length(google_compute_instance.vm_instance) > 0 ? google_compute_instance.vm_instance[0].network_interface[0].network_ip : null
}
