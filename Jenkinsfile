//Este es el Jenkinsfile para el proyecto de Jenkins
pipeline { 
   agent any 
   environment {
      GIT_REPO = 'MP_202510_G81_E3_Caminatas_Back'
      GIT_CREDENTIAL_ID = 'c0e8826e-5e2c-4c1c-a484-6c17d53ac539'
      SONARQUBE_URL = 'http://10.20.84.26:8082/sonar'
      SONAR_TOKEN = credentials('sonar-login')
   }
   stages { 
      stage('Checkout') { 
         steps {
            scmSkip(deleteBuild: true, skipPattern:'.*\\[ci-skip\\].*')

               //git branch: 'main', 
               //No olvidar quitar esta parte del código cuando se haga el merge a main
               //La hice así para hacer las revisiones de los cambios en jenkins
               git branch: 'feature/JuanLopez',
               credentialsId: env.GIT_CREDENTIAL_ID,
               url: 'https://github.com/UDFJDC-ModelosProgramacion/' + env.GIT_REPO
         }
      }
      stage('Build') {
         // Build artifacts
         steps {
            script {
               docker.image('citools-isis2603:latest').inside('-v $HOME/.m2:/root/.m2:z -u root') {
                  sh '''
                     java -version
                     mvn clean install
                  '''
               }
            }
         }
      }
      stage('Testing') {
         // Run unit tests
         steps {
            script {
               docker.image('citools-isis2603:latest').inside('-v $HOME/.m2:/root/.m2:z -u root') {                  
                  sh '''
                     mvn test
                  '''
               }
            }
         }
      }
      stage('Static Analysis') {
         // Run static analysis
         steps {
            script {
               docker.image('citools-isis2603:latest').inside('-v $HOME/.m2:/root/.m2:z -u root') {
                  sh '''
                     mvn sonar:sonar -Dsonar.token=${SONAR_TOKEN} -Dsonar.host.url=${SONARQUBE_URL}
                  '''
               }
            }
         }
      }
   }
   post {
      always {
         node {
            deleteDir()
            dir("${env.GIT_REPO}@tmp") {
               deleteDir()
            }   
         }      
      }
   }
}
