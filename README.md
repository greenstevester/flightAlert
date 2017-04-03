# FlightAlert #

Demonstrates a small example of Spring Boot/Spring Data/Angular 1.x Monolith built using the JHipster framework (https://jhipster.github.io/) which has everything you need!
It notifies you per email when a desired flight Business class fares from Milan Airport. It uses the google flights API (https://www.google.ch/flights/) to 
source the flights.

see https://developers.google.com/qpx-express/v1/trips/search
see http://ourairports.com/data/

HOWTO SETUP CLOUDFOUNDRY
============================
see http://jhipster.github.io/cloudfoundry/
yo jhipster:cloudfoundry

HOWTO SETUP MYSQL ON CLOUDFOUNDRY (CLEARDB)
============================
1. create a Bound service: cleardb-msql-flightalert
2. specify the url e.g.: jdbc:mysql://us-cdbr-iron-east-03.cleardb.net/<your server key>?useUnicode=true&characterEncoding=utf8
   username: <insert yours provided by cloudfoundry>
   password: <insert yours>

HOWTO DEPLOY TO CLOUDFOUNDRY
============================
mvn clean package -DskipTests -Pcloudfoundry
cf push -f ./deploy/cloudfoundry/manifest.yml

Login to the CLI
===================
$ cf login -a https://api.run.pivotal.io
Email: your login
Password: your password

CLOUD FOUNDRY MAIN PAGE
============================
https://console.run.pivotal.io

HOW TO START/STOP/RESTART
============================
cf start flightalert
cf stop flightalert
cf restart flightalert

HOW TO LOOK AT LOGS
============================
cf logs flightalert
cf logs flightalert --recent

HOW TO LOOK AT CF ENV VARIABLES
============================
cf env flightalert

RUNNING LOCALLY
============================
java -jar ./target/flightalert-0.0.1-SNAPSHOT.war --debug --spring.main.show_banner=false --spring.profiles.active=cloud --spring.output.ansi.enabled=always


CREATING A CUSTOM BUILDPACK
============================
http://docs.run.pivotal.io/buildpacks/java/java-tips.html#memory
