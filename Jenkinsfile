node {
    // uncomment these 2 lines and edit the name 'node-4.4.7' according to what you choose in configuration
    // TEST 
    def nodeHome = tool name: 'node-4.4.5', type: 'jenkins.plugins.nodejs.tools.NodeJSInstallation'
    env.PATH = "${nodeHome}/bin:${env.PATH}"

    stage 'check tools'
    sh "whoami"
    sh "node -v"
    sh "npm -v"


    stage 'checkout'
    checkout scm

    stage 'clean'
    sh "./mvnw clean"

    stage 'npm install'
    sh "npm install"
    sh "npm update"
    sh "npm install -g grunt-cli"
    sh "npm install -g gulp-cli"
    
    //stage 'backend tests'
    //sh "./mvnw test"

   // stage 'frontend tests'
    //sh "gulp test"

    stage 'package'
    sh "./mvnw -Pprod  -Dmaven.tests.skip=true package"


   // stage 'sonar analysis'
   // sh "sudo ./mvnw sonar:sonar -Dsonar.host.url=http://ec2-52-23-166-207.compute-1.amazonaws.com/sonar"

    stage 'build docker image'
    sh "sudo docker build -t jzaccone/jzjhipster ."
    
    stage 'push docker image'
    sh "sudo docker push jzaccone/jzjhipster"
    
    stage 'deploy docker container'
    sh "ssh jzattendance 'sudo docker pull jzaccone/jzjhipster'"
    sh "ssh jzattendance 'sudo docker rm -f jzjhipster"
    sh "ssh jzattendance 'sudo docker run -d -p 8080:8080 --name jzjhipster jzaccone/jzjhipster'"
   
}
