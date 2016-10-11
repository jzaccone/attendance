node {
    // uncomment these 2 lines and edit the name 'node-4.4.7' according to what you choose in configuration
    // TEST 
    def nodeHome = tool name: 'node-4.4.5', type: 'jenkins.plugins.nodejs.tools.NodeJSInstallation'
    env.PATH = "${nodeHome}/bin:${env.PATH}"

    stage 'check tools'
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
    
    stage 'backend tests'
    sh "./mvnw test"

    stage 'frontend tests'
    sh "gulp test"

    stage 'package'
    sh "./mvnw -Pprod  -Dmaven.tests.skip=true package"


   // stage 'sonar analysis'
   // sh "sudo ./mvnw sonar:sonar -Dsonar.host.url=http://ec2-52-23-166-207.compute-1.amazonaws.com/sonar"

    stage 'deploy'
    sh "scp target/*.original tomcat:/home/bitnami/stack/apache-tomcat/webapps/devops.war"
    sh "ssh owner 'sudo /home/bitnami/stack/ctlscript.sh restart tomcat'"
}
