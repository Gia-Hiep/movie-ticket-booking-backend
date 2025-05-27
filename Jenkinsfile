pipeline {
         agent any
         tools {
             jdk 'JDK17'
             maven 'Maven3'
             nodejs 'Node20'
         }
         environment {
             STRIPE_API_KEY = credentials('stripe-api-key')
             FIREBASE_CONFIG_PATH = 'C:\\Users\\phamh\\secure-config\\firebase-service-account.json'
         }
         stages {
             stage('Checkout') {
                 steps {
                     git branch: 'main', url: 'https://github.com/Gia-Hiep/movie-ticket-booking-backend.git'
                 }
             }
             stage('Build') {
                 steps {
                     powershell '.\\mvnw clean install -DskipTests'
                 }
             }
             stage('Unit Test') {
                 steps {
                     powershell '.\\mvnw test'
                 }
             }
             stage('API Test') {
                 steps {
                     // Start Spring Boot app in background
                     powershell 'Start-Process -NoNewWindow -FilePath "powershell.exe" -ArgumentList ".\\mvnw spring-boot:run"'
                     // Wait for app to start
                     powershell 'Start-Sleep -Seconds 30'
                     // Run Newman
                     powershell 'newman run MovieBookingAPI.postman_collection.json'
                     // Stop Spring Boot app
                     powershell 'Stop-Process -Name "java" -Force'
                 }
             }
         }
         post {
             always {
                 junit '**/target/surefire-reports/*.xml'
                 publishHTML(target: [
                     reportDir: 'target/surefire-reports',
                     reportFiles: 'index.html',
                     reportName: 'Test Report'
                 ])
             }
             success {
                 echo 'Build and tests passed!'
             }
             failure {
                 echo 'Build or tests failed!'
             }
         }
     }