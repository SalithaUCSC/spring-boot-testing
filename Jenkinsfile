pipeline {
    agent any
    stages {
        stage("init") {
            steps {
                echo "init"
            }
        }
        stage("test") {
            steps {
                echo "test"
            }
        }
        stage("build") {
            steps {
                echo "build"
            }
        }
        stage("deploy") {
            steps {
              echo "deploy"
            }
        }
    }
    environment {
        DB_PASSWORD = credentials('db-password')
        DB_USERNAME = credentials('db-username')
        DB_NAME = 'order-db'
    }
}