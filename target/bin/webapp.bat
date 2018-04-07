@REM ----------------------------------------------------------------------------
@REM  Copyright 2001-2006 The Apache Software Foundation.
@REM
@REM  Licensed under the Apache License, Version 2.0 (the "License");
@REM  you may not use this file except in compliance with the License.
@REM  You may obtain a copy of the License at
@REM
@REM       http://www.apache.org/licenses/LICENSE-2.0
@REM
@REM  Unless required by applicable law or agreed to in writing, software
@REM  distributed under the License is distributed on an "AS IS" BASIS,
@REM  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
@REM  See the License for the specific language governing permissions and
@REM  limitations under the License.
@REM ----------------------------------------------------------------------------
@REM
@REM   Copyright (c) 2001-2006 The Apache Software Foundation.  All rights
@REM   reserved.

@echo off

set ERROR_CODE=0

:init
@REM Decide how to startup depending on the version of windows

@REM -- Win98ME
if NOT "%OS%"=="Windows_NT" goto Win9xArg

@REM set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" @setlocal

@REM -- 4NT shell
if "%eval[2+2]" == "4" goto 4NTArgs

@REM -- Regular WinNT shell
set CMD_LINE_ARGS=%*
goto WinNTGetScriptDir

@REM The 4NT Shell from jp software
:4NTArgs
set CMD_LINE_ARGS=%$
goto WinNTGetScriptDir

:Win9xArg
@REM Slurp the command line arguments.  This loop allows for an unlimited number
@REM of arguments (up to the command line limit, anyway).
set CMD_LINE_ARGS=
:Win9xApp
if %1a==a goto Win9xGetScriptDir
set CMD_LINE_ARGS=%CMD_LINE_ARGS% %1
shift
goto Win9xApp

:Win9xGetScriptDir
set SAVEDIR=%CD%
%0\
cd %0\..\.. 
set BASEDIR=%CD%
cd %SAVEDIR%
set SAVE_DIR=
goto repoSetup

:WinNTGetScriptDir
set BASEDIR=%~dp0\..

:repoSetup
set REPO=


if "%JAVACMD%"=="" set JAVACMD=java

if "%REPO%"=="" set REPO=%BASEDIR%\repo

set CLASSPATH="%BASEDIR%"\etc;"%REPO%"\org\apache\tomcat\embed\tomcat-embed-core\8.5.23\tomcat-embed-core-8.5.23.jar;"%REPO%"\org\apache\tomcat\tomcat-annotations-api\8.5.23\tomcat-annotations-api-8.5.23.jar;"%REPO%"\org\apache\tomcat\embed\tomcat-embed-jasper\8.5.23\tomcat-embed-jasper-8.5.23.jar;"%REPO%"\org\apache\tomcat\embed\tomcat-embed-el\8.5.23\tomcat-embed-el-8.5.23.jar;"%REPO%"\org\eclipse\jdt\ecj\3.12.3\ecj-3.12.3.jar;"%REPO%"\org\apache\tomcat\tomcat-jasper\8.5.23\tomcat-jasper-8.5.23.jar;"%REPO%"\org\apache\tomcat\tomcat-servlet-api\8.5.23\tomcat-servlet-api-8.5.23.jar;"%REPO%"\org\apache\tomcat\tomcat-juli\8.5.23\tomcat-juli-8.5.23.jar;"%REPO%"\org\apache\tomcat\tomcat-el-api\8.5.23\tomcat-el-api-8.5.23.jar;"%REPO%"\org\apache\tomcat\tomcat-api\8.5.23\tomcat-api-8.5.23.jar;"%REPO%"\org\apache\tomcat\tomcat-util-scan\8.5.23\tomcat-util-scan-8.5.23.jar;"%REPO%"\org\apache\tomcat\tomcat-util\8.5.23\tomcat-util-8.5.23.jar;"%REPO%"\org\apache\tomcat\tomcat-jasper-el\8.5.23\tomcat-jasper-el-8.5.23.jar;"%REPO%"\org\apache\tomcat\tomcat-jsp-api\8.5.23\tomcat-jsp-api-8.5.23.jar;"%REPO%"\commons-digester\commons-digester\2.1\commons-digester-2.1.jar;"%REPO%"\commons-beanutils\commons-beanutils\1.8.3\commons-beanutils-1.8.3.jar;"%REPO%"\commons-logging\commons-logging\1.1.1\commons-logging-1.1.1.jar;"%REPO%"\org\glassfish\jersey\containers\jersey-container-servlet\2.10.1\jersey-container-servlet-2.10.1.jar;"%REPO%"\org\glassfish\jersey\containers\jersey-container-servlet-core\2.10.1\jersey-container-servlet-core-2.10.1.jar;"%REPO%"\org\glassfish\hk2\external\javax.inject\2.3.0-b05\javax.inject-2.3.0-b05.jar;"%REPO%"\org\glassfish\jersey\core\jersey-common\2.10.1\jersey-common-2.10.1.jar;"%REPO%"\javax\annotation\javax.annotation-api\1.2\javax.annotation-api-1.2.jar;"%REPO%"\org\glassfish\jersey\bundles\repackaged\jersey-guava\2.10.1\jersey-guava-2.10.1.jar;"%REPO%"\org\glassfish\hk2\hk2-api\2.3.0-b05\hk2-api-2.3.0-b05.jar;"%REPO%"\org\glassfish\hk2\hk2-utils\2.3.0-b05\hk2-utils-2.3.0-b05.jar;"%REPO%"\org\glassfish\hk2\external\aopalliance-repackaged\2.3.0-b05\aopalliance-repackaged-2.3.0-b05.jar;"%REPO%"\org\glassfish\hk2\hk2-locator\2.3.0-b05\hk2-locator-2.3.0-b05.jar;"%REPO%"\org\javassist\javassist\3.18.1-GA\javassist-3.18.1-GA.jar;"%REPO%"\org\glassfish\hk2\osgi-resource-locator\1.0.1\osgi-resource-locator-1.0.1.jar;"%REPO%"\org\glassfish\jersey\core\jersey-server\2.10.1\jersey-server-2.10.1.jar;"%REPO%"\org\glassfish\jersey\core\jersey-client\2.10.1\jersey-client-2.10.1.jar;"%REPO%"\javax\validation\validation-api\1.1.0.Final\validation-api-1.1.0.Final.jar;"%REPO%"\javax\ws\rs\javax.ws.rs-api\2.0\javax.ws.rs-api-2.0.jar;"%REPO%"\org\json\org.json\chargebee-1.0\org.json-chargebee-1.0.jar;"%REPO%"\com\google\api-client\google-api-client\1.22.0\google-api-client-1.22.0.jar;"%REPO%"\com\google\oauth-client\google-oauth-client\1.22.0\google-oauth-client-1.22.0.jar;"%REPO%"\com\google\http-client\google-http-client\1.22.0\google-http-client-1.22.0.jar;"%REPO%"\org\apache\httpcomponents\httpclient\4.0.1\httpclient-4.0.1.jar;"%REPO%"\org\apache\httpcomponents\httpcore\4.0.1\httpcore-4.0.1.jar;"%REPO%"\commons-codec\commons-codec\1.3\commons-codec-1.3.jar;"%REPO%"\com\google\code\findbugs\jsr305\1.3.9\jsr305-1.3.9.jar;"%REPO%"\com\google\http-client\google-http-client-jackson2\1.22.0\google-http-client-jackson2-1.22.0.jar;"%REPO%"\com\fasterxml\jackson\core\jackson-core\2.1.3\jackson-core-2.1.3.jar;"%REPO%"\com\google\guava\guava-jdk5\17.0\guava-jdk5-17.0.jar;"%REPO%"\org\postgresql\postgresql\9.4.1208\postgresql-9.4.1208.jar;"%REPO%"\javax\mail\mail\1.4.6-rc1\mail-1.4.6-rc1.jar;"%REPO%"\javax\activation\activation\1.1\activation-1.1.jar;"%REPO%"\org\apache\tomcat\catalina-ha\6.0.16\catalina-ha-6.0.16.jar;"%REPO%"\org\apache\tomcat\coyote\6.0.16\coyote-6.0.16.jar;"%REPO%"\org\apache\tomcat\servlet-api\6.0.16\servlet-api-6.0.16.jar;"%REPO%"\org\apache\tomcat\juli\6.0.16\juli-6.0.16.jar;"%REPO%"\org\apache\tomcat\tribes\6.0.16\tribes-6.0.16.jar;"%REPO%"\org\apache\tomcat\catalina\6.0.16\catalina-6.0.16.jar;"%REPO%"\org\apache\tomcat\annotations-api\6.0.16\annotations-api-6.0.16.jar;"%REPO%"\assetx\assetx\1.0\assetx-1.0.jar;"%REPO%"\javax\javaee-api\7.0\javaee-api-7.0.jar;"%REPO%"\com\sun\mail\javax.mail\1.5.0\javax.mail-1.5.0.jar;"%REPO%"\com\altervista\ipq\asset-max\1.0-SNAPSHOT\asset-max-1.0-SNAPSHOT.jar

set ENDORSED_DIR=
if NOT "%ENDORSED_DIR%" == "" set CLASSPATH="%BASEDIR%"\%ENDORSED_DIR%\*;%CLASSPATH%

if NOT "%CLASSPATH_PREFIX%" == "" set CLASSPATH=%CLASSPATH_PREFIX%;%CLASSPATH%

@REM Reaching here means variables are defined and arguments have been captured
:endInit

%JAVACMD% %JAVA_OPTS%  -classpath %CLASSPATH% -Dapp.name="webapp" -Dapp.repo="%REPO%" -Dapp.home="%BASEDIR%" -Dbasedir="%BASEDIR%" launch.Main %CMD_LINE_ARGS%
if %ERRORLEVEL% NEQ 0 goto error
goto end

:error
if "%OS%"=="Windows_NT" @endlocal
set ERROR_CODE=%ERRORLEVEL%

:end
@REM set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" goto endNT

@REM For old DOS remove the set variables from ENV - we assume they were not set
@REM before we started - at least we don't leave any baggage around
set CMD_LINE_ARGS=
goto postExec

:endNT
@REM If error code is set to 1 then the endlocal was done already in :error.
if %ERROR_CODE% EQU 0 @endlocal


:postExec

if "%FORCE_EXIT_ON_ERROR%" == "on" (
  if %ERROR_CODE% NEQ 0 exit %ERROR_CODE%
)

exit /B %ERROR_CODE%
