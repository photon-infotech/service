@REM
@REM Phresco Service Root
@REM
@REM Copyright (C) 1999-2014 Photon Infotech Inc.
@REM
@REM Licensed under the Apache License, Version 2.0 (the "License");
@REM you may not use this file except in compliance with the License.
@REM You may obtain a copy of the License at
@REM
@REM         http://www.apache.org/licenses/LICENSE-2.0
@REM
@REM Unless required by applicable law or agreed to in writing, software
@REM distributed under the License is distributed on an "AS IS" BASIS,
@REM WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
@REM See the License for the specific language governing permissions and
@REM limitations under the License.
@REM

set REPO_URL=http://localhost:8080/nexus-webapp-1.9.2.2/content/repositories/repository/
set username=admin
set password=admin123

call mvn install:install-file -DrepositoryId=repository -DartifactId=repository -Durl=http://localhost:8080/nexus-webapp-1.9.2.2/content/repositories/repository/ -Dfile=../tools/pom.xml -DgroupId=com.photon.phresco.repository -Dversion=1.0-SNAPSHOT -Dpackaging=pom

call mvn deploy:deploy-file -DgroupId=com.photon.phresco.plugins -DartifactId=drupal-maven-plugin -Dversion=1.0 -Dpackaging=maven-plugin -Dfile=../../plugins/drupal-maven-plugin/pom.xml -DrepositoryId=repository -Durl=%REPO_URL%

call mvn deploy:deploy-file -DgroupId=com.photon.phresco.plugins -DartifactId=php-maven-plugin -Dversion=1.0 -Dpackaging=maven-plugin -Dfile=../../plugins/php-maven-plugin/pom.xml -DrepositoryId=repository -Durl=%REPO_URL%

call mvn deploy:deploy-file -DgroupId=archetypes -DartifactId=phresco-sharepoint-archetype -Dversion=1.0 -Dpackaging=jar -Dfile=../../archetypes/phresco-sharepoint-archetype/pom.xml -DrepositoryId=repository -Durl=%REPO_URL%

call mvn deploy:deploy-file -DgroupId=archetypes -DartifactId=phresco-drupal7-archetype -Dversion=1.0 -Dpackaging=jar -Dfile=../../archetypes/phresco-drupal7-archetype/pom.xml -DrepositoryId=repository -Durl=%REPO_URL%

call mvn deploy:deploy-file -DgroupId=archetypes -DartifactId=phresco-iphone-archetype -Dversion=1.0 -Dpackaging=jar -Dfile=../../archetypes/phresco-iphone-archetype/pom.xml -DrepositoryId=repository -Durl=%REPO_URL%

call mvn deploy:deploy-file -DgroupId=archetypes -DartifactId=phresco-javawebservice-archetype -Dversion=1.0 -Dpackaging=jar -Dfile=../../archetypes/phresco-javawebservice-archetype/pom.xml -DrepositoryId=%REPO_URL%

call mvn deploy:deploy-file -DgroupId=archetypes -DartifactId=phresco-nodejs-archetype -Dversion=1.0 -Dpackaging=jar -Dfile=../../archetypes/phresco-nodejs-archetype/pom.xml -DrepositoryId=repository -Durl=%REPO_URL%

call mvn deploy:deploy-file -DgroupId=archetypes -DartifactId=phresco-php-archetype -Dversion=1.0 -Dpackaging=jar -Dfile=../../archetypes/phresco-php-archetype/pom.xml -DrepositoryId=repository -Durl=%REPO_URL%
