resource "google_compute_firewall" "allow_web_ssh" {
  name    = "${var.project_name}-allow-web-ssh-${var.environment}"
  network = var.network_name

  allow {
    protocol = "tcp"
    ports    = ["22", "80", "443"]
  }

  source_ranges = ["0.0.0.0/0"]
  target_tags   = ["${var.project_name}-${var.environment}"]
}
