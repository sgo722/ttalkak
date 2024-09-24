pipeline {
    agent any

    environment {
                GITLAB_CREDENTIALS_ID = 'GITLAB_CREDENTIALS_ID' // GitLab 인증 정보 ID
                GITHUB_TOKEN = 'GITHUB_TOKEN' // GitLab 인증 정보 ID
        }

    stages {
        stage('Checkout') {
            steps {
                // Git 리포지토리에서 코드를 체크아웃합니다.
                checkout scm
                script {
                    dir('config-server') {
                        sh 'mkdir -p src/main/resources'
                        sh 'cp /home/ubuntu/secret/config/application.yml src/main/resources'
                    }
                }
            }
        }

        stage('Test Services') {
            parallel {
                // stage('Test Eureka Server') {
                //     when {
                //         changeset "eureka-server/**"
                //     }
                //     steps {
                //         script {
                //             // Eureka Server 테스트 코드 실행
                //             sh """
                //             cd eureka-server
                //             chmod +x gradlew
                //             ./gradlew test
                //             """
                //         }
                //     }
                // }

                // stage('Test Config Server') {
                //     when {
                //         changeset "config-server/**"
                //     }
                //     steps {
                //         script {
                //             // Config Server 테스트 코드 실행
                //             sh """
                //             cd config-server
                //             chmod +x gradlew
                //             ./gradlew test
                //             """
                //         }
                //     }
                // }

                // stage('Test Gateway Service') {
                //     when {
                //         changeset "gateway-service/**"
                //     }
                //     steps {
                //         script {
                //             // Gateway Service 테스트 코드 실행
                //             sh """
                //             cd gateway-service
                //             chmod +x gradlew
                //             ./gradlew test
                //             """
                //         }
                //     }
                // }

                stage('Test User Service') {
                    when {
                        changeset "user-service/**"
                    }
                    steps {
                        script {
                            // User Service 테스트 코드 실행
                            sh """
                            cd user-service
                            chmod +x gradlew
                            ./gradlew test
                            ./gradlew openapi3-security-schemes
                            """
                        }
                    }
                }

                // stage('Test Compute Service') {
                //     when {
                //         changeset "compute-service/**"
                //     }
                //     steps {
                //         script {
                //             // Compute Service 테스트 코드 실행
                //             sh """
                //             cd compute-service
                //             chmod +x gradlew
                //             ./gradlew test
                //             """
                //         }
                //     }
                // }

                stage('Test Deployment Service') {
                    when {
                        changeset "deployment-service/**"
                    }
                    steps {
                        script {
                            // Deployment Service 테스트 코드 실행
                            sh """
                            cd deployment-service
                            chmod +x gradlew
                            ./gradlew test
                            ./gradlew openapi3-security-schemes
                            """
                        }
                    }
                }

                stage('Test Project Service') {
                    when {
                        changeset "project-service/**"
                    }
                    steps {
                        script {
                            // Project Service 테스트 코드 실행
                            sh """
                            cd project-service
                            chmod +x gradlew
                            ./gradlew test
                            ./gradlew openapi3-security-schemes
                            """
                        }
                    }
                }
            }
        }

        stage('documentation') {
            when {
                anyOf {
                    changeset "deployment-service/**"
                    changeset "project-service/**"
                    changeset "user-service/**"
                }
            }
            steps {
                sh """
                docker ps -q --filter "name=document-service" | xargs -r docker stop
                docker ps -aq --filter "name=document-service" | xargs -r docker rm

                sudo cp /var/lib/jenkins/workspace/ttalkak/deployment-service/build/resources/test/docs/ttalkak-deployment-api-docs.yaml /var/lib/jenkins/workspace/ttalkak/docs/deployment-api-docs.yaml
                sudo cp /var/lib/jenkins/workspace/ttalkak/project-service/build/resources/test/docs/ttalkak-project-api-docs.yaml /var/lib/jenkins/workspace/ttalkak/docs/project-api-docs.yaml
                sudo cp /var/lib/jenkins/workspace/ttalkak/user-service/build/resources/test/docs/ttalkak-user-api-docs.yaml /var/lib/jenkins/workspace/ttalkak/docs/user-api-docs.yaml

                docker run -d \
                  --name document-service \
                  -p 10000:8080 \
                  -e URLS_PRIMARY_NAME=User \
                  -e URLS="[ { url: '/docs/user-api-docs.yaml', name: 'User' },{ url: '/docs/deployment-api-docs.yaml', name: 'Deployment' }, { url: '/docs/project-api-docs.yaml', name: 'Project' } ]" \
                  -v /var/lib/jenkins/workspace/ttalkak/docs:/usr/share/nginx/html/docs/ \
                  swaggerapi/swagger-ui
                  """
            }
        }

        stage('Build and Deploy Services') {
            parallel {
                stage('Build and Deploy Eureka Server') {
                    when {
                        changeset "eureka-server/**"
                    }
                    steps {
                        script {

                            // 기존 eureka-server 컨테이너 중지 및 삭제
                            sh """
                            docker-compose -f docker-compose-prod.yml stop eureka-server || true
                            docker-compose -f docker-compose-prod.yml rm -f eureka-server || true
                            """

                            // 해당 컨테이너만 재시작
                            sh """
                            docker-compose -f docker-compose-prod.yml build --no-cache eureka-server
                            docker-compose -f docker-compose-prod.yml up -d --no-deps --build eureka-server
                            """
                        }
                    }
                }

                stage('Build and Deploy Config Server') {
                    when {
                        changeset "config-server/**"
                    }
                    steps {
                        script {

                            // 기존 config-server 컨테이너 중지 및 삭제
                            sh """
                            docker-compose -f docker-compose-prod.yml stop config-server || true
                            docker-compose -f docker-compose-prod.yml rm -f config-server || true
                            """

                            // 해당 컨테이너만 재시작
                            sh """
                            docker-compose -f docker-compose-prod.yml build --no-cache config-server
                            docker-compose -f docker-compose-prod.yml up -d --no-deps --build config-server
                            """
                        }
                    }
                }

                stage('Build and Deploy Notification Server') {
                    when {
                        changeset "notification-service/**"
                    }
                    steps {
                        script {

                            // 기존 notification-server 컨테이너 중지 및 삭제
                            sh """
                            docker-compose -f docker-compose-prod.yml stop notification-service || true
                            docker-compose -f docker-compose-prod.yml rm -f notification-service || true
                            """

                            // 해당 컨테이너만 재시작
                            sh """
                            docker-compose -f docker-compose-prod.yml build --no-cache notification-service
                            docker-compose -f docker-compose-prod.yml up -d --no-deps --build notification-service
                            """
                        }
                    }
                }

                stage('Build and Deploy Gateway Service') {
                    when {
                        changeset "gateway-service/**"
                    }
                    steps {
                        script {

                            // 기존 user-service 컨테이너 중지 및 삭제
                            sh """
                            docker-compose -f docker-compose-prod.yml stop gateway-service || true
                            docker-compose -f docker-compose-prod.yml rm -f gateway-service || true
                            """

                            // 해당 컨테이너만 재시작
                            sh """
                            docker-compose -f docker-compose-prod.yml build --no-cache gateway-service
                            docker-compose -f docker-compose-prod.yml up -d --no-deps --build gateway-service
                            """
                        }
                    }
                }

                stage('Build and Deploy User Service') {
                    when {
                        changeset "user-service/**"
                    }
                    steps {
                        script {

                            // 기존 user-service 컨테이너 중지 및 삭제
                            sh """
                            docker-compose -f docker-compose-prod.yml stop user-service || true
                            docker-compose -f docker-compose-prod.yml rm -f user-service || true
                            """

                            // 해당 컨테이너만 재시작
                            sh """
                            docker-compose -f docker-compose-prod.yml build --no-cache user-service
                            docker-compose -f docker-compose-prod.yml up -d --no-deps --build user-service
                            """
                        }
                    }
                }

                stage('Build and Deploy Compute Service') {
                    when {
                        changeset "compute-service/**"
                    }
                    steps {
                        script {

                            // 기존 user-service 컨테이너 중지 및 삭제
                            sh """
                            docker-compose -f docker-compose-prod.yml stop compute-service || true
                            docker-compose -f docker-compose-prod.yml rm -f compute-service || true
                            """

                            // 해당 컨테이너만 재시작
                            sh """
                            docker-compose -f docker-compose-prod.yml build --no-cache compute-service
                            docker-compose -f docker-compose-prod.yml up -d --no-deps --build compute-service
                            """
                        }
                    }
                }


                stage('Build and Deploy Deployment Service') {
                    when {
                        changeset "deployment-service/**"
                    }
                    steps {
                        script {

                            // 기존 user-service 컨테이너 중지 및 삭제
                            sh """
                            docker-compose -f docker-compose-prod.yml stop deployment-service || true
                            docker-compose -f docker-compose-prod.yml rm -f deployment-service || true
                            """

                            // 해당 컨테이너만 재시작
                            sh """
                            docker-compose -f docker-compose-prod.yml build --no-cache deployment-service
                            docker-compose -f docker-compose-prod.yml up -d --no-deps --build deployment-service
                            """
                        }
                    }
                }


                stage('Build and Deploy Project Service') {
                    when {
                        changeset "project-service/**"
                    }
                    steps {
                        script {

                            // 기존 user-service 컨테이너 중지 및 삭제
                            sh """
                            docker-compose -f docker-compose-prod.yml stop project-service || true
                            docker-compose -f docker-compose-prod.yml rm -f project-service || true
                            """

                            // 해당 컨테이너만 재시작
                            sh """
                            docker-compose -f docker-compose-prod.yml build --no-cache project-service
                            docker-compose -f docker-compose-prod.yml up -d --no-deps --build project-service
                            """
                        }
                    }
                }
            }
        }

        stage('Update GitLab Repository') {
            steps {
                withCredentials([usernamePassword(credentialsId: GITLAB_CREDENTIALS_ID, passwordVariable: 'GITLAB_PASSWORD', usernameVariable: 'GITLAB_USERNAME'),
                                 string(credentialsId: GITHUB_TOKEN, variable: 'GITHUB_TOKEN')]) {
                    sh '''
                            git config --global user.email "sgo722@naver.com"
                            git config --global user.name "sgo722"

                            # Clone GitLab repository
                            rm -rf S11P21C108
                            git clone https://oauth2:${GITLAB_PASSWORD}@lab.ssafy.com/s11-blochain-transaction-sub1/S11P21C108.git
                            cd S11P21C108

                            git fetch --all

                            git reset --hard origin/master

                            git subtree pull --prefix=config https://${GITHUB_TOKEN}@github.com/sunsuking/ddalkak_config.git main --squash
                            git subtree pull --prefix=tunneling https://${GITHUB_TOKEN}@github.com/sunsuking/tunelling.git master --squash
                            git subtree pull --prefix=config https://${GITHUB_TOKEN}@github.com/sunsuking/ddalkak_config.git main --squash
                            git subtree pull --prefix=server https://${GITHUB_TOKEN}@github.com/sunsuking/ddalkak.git master --squash
                            git subtree pull --prefix=client https://${GITHUB_TOKEN}@github.com/ljjunh/ttalkak.git master --squash

                            # Set remote URL for GitLab
                            git clone https://oauth2:${GITLAB_PASSWORD}@lab.ssafy.com/s11-blochain-transaction-sub1/S11P21C108.git

                            # Ensure there are changes to commit and force push
                            git add .
                            git commit -m "Update subtrees" || true
                            git push --force origin master
                        '''
                }
            }
        }
    }
}
