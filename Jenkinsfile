#!/usr/bin/env groovy

final RELEASE_VERSION = 'releaseVersion'
final RELEASE_VERSION_DEFAULT = ''
final DEVELOPMENT_VERSION = 'developmentVersion'
final DEVELOPMENT_VERSION_DEFAULT = ''
final ENABLE_RELEASE = 'enableRelease'
final ENABLE_RELEASE_DEFAULT = false

final GIT_COMMITER_EMAIL = 'cam-team@mulesoft.com'
final GIT_COMMITER_USERNAME = 'cam-team'

def pipelineProperties = [
    parameters([
        booleanParam(
            name: ENABLE_RELEASE,
            description: 'Flag to enable release functionality',
            defaultValue: ENABLE_RELEASE_DEFAULT
        ),
        string(
            name: RELEASE_VERSION,
            description: 'Version to be released',
            defaultValue: RELEASE_VERSION_DEFAULT
        ),
        string(
            name: DEVELOPMENT_VERSION,
            description: 'Version to be used after release',
            defaultValue: DEVELOPMENT_VERSION_DEFAULT
        )
    ]),
    [
        $class: 'BuildDiscarderProperty',
        strategy: [$class: 'LogRotator', artifactDaysToKeepStr: '', artifactNumToKeepStr: '', daysToKeepStr: '', numToKeepStr: '10']
    ]
]

def isBuildTriggeredByRelease() {
    result = sh (script: "git log -1 | grep '\\[maven-release-plugin\\] prepare release'", returnStatus: true)
    return (result == 0)
}

node {
    checkout scm

    def mvnHome = ""
    def jdkHome = ""

    def branchIs = { branchName -> env.BRANCH_NAME ==~ branchName }
    def branchIsNot = { branchName -> !branchIs(branchName) }

    properties(pipelineProperties);

    withCredentials([
            [
                    $class: 'UsernamePasswordMultiBinding',
                    credentialsId: 'nexus',
                    passwordVariable: 'NEXUS_PASS',
                    usernameVariable: 'NEXUS_USER'
            ],
            [
                    $class: 'UsernamePasswordMultiBinding',
                    credentialsId: 'nexus-public',
                    passwordVariable: 'PUB_NEXUS_PASS',
                    usernameVariable: 'PUB_NEXUS_USER'
            ]
    ]) {

        stage("Validate Parameters") {
            echo 'Parameter ' + ENABLE_RELEASE + ' = ' + params[ENABLE_RELEASE]
            echo 'Parameter ' + RELEASE_VERSION + ' = ' + params[RELEASE_VERSION]
            echo 'Parameter ' + DEVELOPMENT_VERSION + ' = ' + params[DEVELOPMENT_VERSION]

            if (branchIsNot('master') && params[ENABLE_RELEASE]) {
                error('Release functionality is only available from master branch')
            }
            if (params[ENABLE_RELEASE] && (params[RELEASE_VERSION] == '' || params[DEVELOPMENT_VERSION] == '')) {
                error('Release functionality needs both parameters to be set: ' + RELEASE_VERSION + ' and ' + DEVELOPMENT_VERSION)
            }
        }

        stage("Checkout") {
            checkoutSCM(scm)
        }

        stage("Check Release Triggered Build") {
            env.RELEASE_TRIGGERED = "false"
            if (isBuildTriggeredByRelease()) {
                env.RELEASE_TRIGGERED = "true"
                echo "Build was triggered by Release"
            }
        }

        if (env.RELEASE_TRIGGERED == "true") {
           echo 'Aborting Release triggered Build'
           currentBuild.result = 'ABORTED'
           return
        }

        stage("Set Maven configuration") {
            env.MAVEN_SETTINGS_PATH = '.jenkins/settings.xml'
            env.MAVEN_SETTINGS_PATH

            mvnHome = tool name: 'maven-3.3.9', type: 'hudson.tasks.Maven$MavenInstallation'
            jdkHome = tool name: 'Java 8', type: 'hudson.model.JDK'
        }

        stage("Build & Test") {
            withEnv(["PATH=${jdkHome}/bin:${mvnHome}/bin:${env.PATH}", "M2_HOME=${mvnHome}"]) {
                echo sh(script: 'env|sort', returnStdout: true)
                sh "mvn -s ${env.MAVEN_SETTINGS_PATH} clean verify -U"
            }
        }

/*
        stage ("Nexus IQ") {
            withEnv(["PATH=${jdkHome}/bin:${mvnHome}/bin:${env.PATH}", "M2_HOME=${mvnHome}"]) {
                nexusIQScan("b2b-solution-services",
                "http://nexus-iq-0:8070",
                "b2b-solution-model-jooq/jooq-model-partners-service/target/jooq-model-partners-service-*.jar")
            }
        }

        stage("Regular Deploy") {
            if (branchIs('master') && !params[ENABLE_RELEASE]) {
                echo 'Running Regular Deploy'

                withEnv(["PATH=${jdkHome}/bin:${mvnHome}/bin:${env.PATH}", "M2_HOME=${mvnHome}"]) {
                    echo sh(script: 'env|sort', returnStdout: true)
                    sh "mvn -s ${env.MAVEN_SETTINGS_PATH} -U clean deploy -DskipTests"
                }
            } else if (branchIs('develop')){
                echo 'Running SNAPSHOT Deploy'

                withEnv(["PATH=${jdkHome}/bin:${mvnHome}/bin:${env.PATH}", "M2_HOME=${mvnHome}"]) {
                    echo sh(script: 'env|sort', returnStdout: true)
                    sh "mvn -s ${env.MAVEN_SETTINGS_PATH} -U clean deploy -DskipTests -DaltReleaseDeploymentRepository='fake-repo::default::http://asd.example.com/api/maven' "
                }
            } else {
                echo 'Skipping Regular Deploy'
            }
        }

        stage("Release") {
            if (branchIs('master') && params[ENABLE_RELEASE]) {
                echo 'Running Release'

                withEnv(["PATH=${jdkHome}/bin:${mvnHome}/bin:${env.PATH}", "M2_HOME=${mvnHome}"]) {
                    echo sh(script: 'env|sort', returnStdout: true)

                    sh "git config user.email ${GIT_COMMITER_EMAIL}"
                    sh "git config user.name '${GIT_COMMITER_USERNAME}'"

                    sh ("mvn -s ${env.MAVEN_SETTINGS_PATH} -U" +
                        " -DreleaseVersion=${params[RELEASE_VERSION]}" +
                        " -DdevelopmentVersion=${params[DEVELOPMENT_VERSION]}" +
                        " -Dgoals=deploy" +
                        " -Darguments='-DskipTests'" +
                        " release:clean release:prepare release:perform"
                    )
                }
            } else {
                echo 'Skipping Release'
            }
        }
        */
    }
}