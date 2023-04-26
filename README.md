# AcrobatSign-DocGenAPIs-Integration
A sample web application has been created to showcase the integration of Document Generation - PDF services and Acrobat Sign.

### Use case : 
This application is developed showcase the integration of Document Generation - PDF services and Acrobat Sign. The application can be re-used for development/integration with other web applications.

# Customer problem to be solved

Following operations are available with the application:
  <ul>
  <li>
    Combine PDF.
  </li>
    <li>
    Re-arrange pages in PDF. 
  </li>
  <li> Generate PDF from word template.
  </li>
   <li> Generate PDF from word template and send for signature.
  </li>
   <li> Send for signature.
  </li>
  </ul>

## Current vs Proposed solution

### Current process
 There is no solution available to show the end to end flow using PDF/Doc-Gen services with Acrobat Sign.

### Proposed solution

<ul>
  <li>
    This application provides users to have a quick understanding of the PDF/Doc-Gen services integration with Acrobat Sign. The code is re-usable and can be embedded within the existing web application or integrated using Rest APIs.
  </li>
  </ul>

## Technology stack
  <ul>
     <li>Java 1.8 </li>
     <li>Spring Boot 2.6.7 </li>
  </ul>

# Instructions to run the application
 <ul>
     <li>Please ensure that JDK 1.8 or newer version of Java is installed on the machine.</li>
     <li>Create PDF services credential - https://documentservices.adobe.com/dc-integration-creation-app-cdn/main.html?api=pdf-services-api </li>
     <li> Download the JSON file with in the application folder </li>
     <li>Create integration key - https://helpx.adobe.com/sign/kb/how-to-create-an-integration-key.html </li>
     <li>Update application.yml file with:
       <ul>
        <li>correct integration-key</li>
       </ul>
      </li>
     <li>Edit application.bat file in notepad and update the -Dspring.config.location with the application.yml path and save.</li>
     <li>Run application.bat  OR Run below Command from Command prompt::  java -jar -Dspring.config.location=<path-to-application.yml file> target/acrobatsignbulkoperationtool-0.0.1-SNAPSHOT.jar</li>
  </ul>

# Instructions on how to run the code (For developers)
## Prerequisites
For the building of this project, the client machine should have the following software installed:
<ul>
  <li>
    OS: Windows/Mac/Linux
  </li>
  <li>
    Java JDK: version 1.8
  </li>
  <li>
    Maven: 3.3.3 version or above. Download URL: https://maven.apache.org/download.cgi
  </li>
</ul>
  
## Installation
To install the application to your local repository:
<ul>
  <li>
    Please add JAVA_HOME as an environment variable and set path.
  </li>
  <li>
    Open the run.bat file from the docsigning project
  </li>
  <li>
    update the JAVA_HOME and MAVEN_HOME path based on your local directory. Save and Close
  </li>
  <li> 
    double click on the run.bat file
  </li>
  <li> 
     Application will run as http://localhost:8090/
  </li>
</ul>

## Documentation for API Endpoints
 Swagger documentation is available. please deploy this spring boot application and use below URL for swagger.
http://localhost:8090/swagger-ui.html#/

# Future automation opportunities
<ul>
   <li>
      OAuth 2.0 setup
   </li>
  </ul>
