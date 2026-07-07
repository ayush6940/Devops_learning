resource "google_compute_instance" "vm_instance" {
  count        = var.create_compute ? 1 : 0
  name         = "${var.project_name}-vm-${var.environment}"
  
  # e2-micro is the only Free Tier eligible Compute Engine machine type
  machine_type = "e2-micro"
  
  # Ensure the VM is deployed in a specific zone of the region
  zone         = "${var.region}-a"

  # Network tags to apply firewall rules
  tags = ["${var.project_name}-${var.environment}"]

  # Resource Labels for billing and management grouping
  labels = {
    environment = var.environment
    project     = var.project_name
    managed-by  = "terraform"
    owner       = "devops"
  }

  boot_disk {
    initialize_params {
      # Debian 12 is a stable, lightweight OS
      image = "debian-cloud/debian-12"
      
      # 30 GB standard persistent disk is exactly the Free Tier limit
      size  = 30 
      type  = "pd-standard"
    }
  }

  network_interface {
    network = var.network_name
    access_config {
      # This block gives the VM an ephemeral public IP address
    }
  }

  service_account {
    # Run the VM under the specific least-privilege service account
    email  = var.service_account_email
    scopes = ["cloud-platform"]
  }

  # Provide the startup script that installs Docker and creates directories
  metadata_startup_script = var.startup_script
}
