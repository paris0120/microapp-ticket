# Ticket Tracking Server

This application is a "microservice" application intended to be part of a microservice architecture.
This application is configured for Service Discovery and Configuration with Consul. On launch, it will refuse to start if it is not able to connect to Consul at [http://localhost:8500](http://localhost:8500).

### Server Dependency

1. Gateway
2. Comment Server
3. Organization Server
4. Tag Server

### Additional libraries for Gateway

1. timeago
2. antd

### TODO

1. Save tags from kafka
2. Save Employees from kafka
3. Add comments to issue detail page
